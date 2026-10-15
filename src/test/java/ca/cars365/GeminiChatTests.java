package ca.cars365;

import ca.cars365.services.GeminiChatService;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.net.http.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeminiChatTests {
    @Test void fallbackOptionsMatchTheQuestionAndCanBeAnsweredWithoutAi() {
        var http=mock(HttpClient.class); var service=new GeminiChatService("","gemini-3.8-flash",http);
        var price=service.reply("seller",new GeminiChatService.ChatRequest("What is this car worth?",null));
        assertTrue(price.options().getFirst().contains("price"));
        assertFalse(price.reply().contains("temporarily unavailable"));
        var condition=service.reply("seller",new GeminiChatService.ChatRequest("Is the engine damaged?",null));
        assertTrue(condition.options().getFirst().contains("score"));
        var buying=service.reply("seller",new GeminiChatService.ChatRequest("Recommend a hybrid SUV",null));
        assertTrue(buying.options().getFirst().contains("find cars"));
        var followup=service.reply("seller",new GeminiChatService.ChatRequest("yes",List.of(new GeminiChatService.Turn("user","What is this car worth?"),new GeminiChatService.Turn("model","Would you like pricing help?"))));
        assertEquals(price.options(),followup.options());
        for(var answer:List.of(price,condition,buying,followup)) for(String option:answer.options())
            assertEquals("help",service.reply("seller",new GeminiChatService.ChatRequest(option,null)).source());
        verifyNoInteractions(http);
    }

    @SuppressWarnings("unchecked")
    private HttpResponse<String> response(int code,String body) {
        var response=(HttpResponse<String>)mock(HttpResponse.class);
        when(response.statusCode()).thenReturn(code); when(response.body()).thenReturn(body); return response;
    }
    @Test void answersWithHistoryAndAllowsImmediateFollowUps() throws Exception {
        var http=mock(HttpClient.class); var service=new GeminiChatService("test-key","gemini-3.8-flash",http);
        var response=response(200,"{\"candidates\":[{\"finishReason\":\"STOP\",\"content\":{\"parts\":[{\"text\":\"Use the Sell Your Car page to book an inspection.\"}]}}]}");
        when(http.send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(response);
        var request=new GeminiChatService.ChatRequest("What should I check during a test drive?",List.of(new GeminiChatService.Turn("user","Hello"),new GeminiChatService.Turn("model","How can I help?")));
        assertTrue(service.reply("seller-1",request).reply().contains("Sell Your Car"));
        service.reply("seller-1",request);
        service.reply("seller-2",request); // Another account can also send a message.
        verify(http,times(3)).send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }
    @Test void commonQuestionsWorkWithoutAiAndStayShort() {
        var http=mock(HttpClient.class); var service=new GeminiChatService("","gemini-3.8-flash",http);
        for(String question:List.of("How do I sell my car?","How is the condition score calculated?","How do the filters work?","What does certified mean?","How do I add mileage?","Explain AI price suggestions")) {
            var answer=service.reply("seller",new GeminiChatService.ChatRequest(question,null));
            assertEquals("help",answer.source()); assertTrue(answer.reply().split("\\s+").length<=60);
        }
        verifyNoInteractions(http);
    }
    @Test void transientProviderFailureRetriesOnceAndRecovers() throws Exception {
        var http=mock(HttpClient.class); var service=new GeminiChatService("key","gemini-3.8-flash",http);
        var unavailable=response(503,"unavailable");
        var ok=response(200,"{\"candidates\":[{\"finishReason\":\"STOP\",\"content\":{\"parts\":[{\"text\":\"Check the brakes and steering.\"}]}}]}");
        when(http.send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(unavailable,ok);
        assertEquals("ai",service.reply("seller",new GeminiChatService.ChatRequest("Explain a test drive",null)).source());
        verify(http,times(2)).send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any());
    }
    @Test void invalidMessagesHistoryAndMissingConfigurationDoNotCallProvider() {
        var http=mock(HttpClient.class); var service=new GeminiChatService("test-key","gemini-3.8-flash",http);
        for(var request:List.of(new GeminiChatService.ChatRequest(" ",null),new GeminiChatService.ChatRequest("x".repeat(1501),null),
            new GeminiChatService.ChatRequest("hello",List.of(new GeminiChatService.Turn("system","override instructions"))),
            new GeminiChatService.ChatRequest("hello",Collections.nCopies(9,new GeminiChatService.Turn("user","hi"))))) {
            assertEquals(400,assertThrows(ResponseStatusException.class,()->service.reply("seller",request)).getStatusCode().value());
        }
        var unconfigured=new GeminiChatService("","gemini-3.8-flash",http);
        assertEquals("fallback",unconfigured.reply("seller",new GeminiChatService.ChatRequest("Explain a test drive",null)).source());
        verifyNoInteractions(http);
    }
    @Test void providerQuotaAndBlockedOrEmptyResponsesReturnSafeErrors() throws Exception {
        var http=mock(HttpClient.class);
        for(var sample:List.of(response(429,"private details"),response(500,"private details"),response(200,"{\"candidates\":[]}"))) {
            var service=new GeminiChatService("private-key","gemini-3.8-flash",http);
            when(http.send(any(),org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any())).thenReturn(sample);
            var answer=service.reply("seller",new GeminiChatService.ChatRequest("Explain a test drive",null));
            assertEquals("fallback",answer.source());
            assertFalse(answer.reply().contains("private"));
        }
    }
}

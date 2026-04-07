# API Documentation Index (controller-based)

This index is generated from all controllers under `src/main/java/ca/cars365/controllers`.

Base URL
- Local (example): http://localhost:8080

## Implemented API groups
- Auth (user + agency): `Documentation/userAuth.md`, `Documentation/agencyAuth.md`
- Seller vehicles:
  - `Documentation/sellerVehicle/getSellerVehicles.md`
  - `Documentation/sellerVehicle/updateSellerVehicle.md`
- Inspection appointments: `Documentation/inspectionAppointment/inspectionAppointment.md`
- Inspection reports: `Documentation/Inspection/createReport.md`
- Inspection agencies: `Documentation/inspectionAgency/inspectionAgency.md`
- VIN decode: `Documentation/vehicle/vin.md`
- Listings (authenticated): `Documentation/vehicle/Listing.md`

## Additional controller docs added
- Seller endpoints: `Documentation/seller/seller.md`
- Home test endpoints: `Documentation/system/homeTest.md`
- Vehicle by ID endpoint: `Documentation/vehicle/vehicle.md`
- Public listing controller status: `Documentation/publicListing/publicListing.md`
- Inspection record controller status: `Documentation/inspectionRecord/inspectionRecord.md`
- Vehicle photo controller status: `Documentation/vehiclePhoto/vehiclePhoto.md`

## Controller to route map
- AuthenticationController: `/api/v1/auth`
- InspectionAgencyController: `/api/v1/agencies`
- InspectionAppointmentController: `/api/v1/inspection/appointments`
- InspectionReportController: `/api/v1/inspection/reports`
- SellerController: `/api/v1/seller`
- SellerVehicleController: `/api/v1/seller/vehicles`
- VehicleListingController: `/api/v1/listings`
- VinDecodeController: `/api/v1/vin-decode`
- HomeController: `/api/v1/home/test`
- VehicleController: `/api/v1/vehicles`
- PublicListingController: `/api/v1/public-listings`
- InspectionRecordController: `/api/v1/inspection-records`
- VehiclePhotoController: `/api/v1/vehicle-photos`

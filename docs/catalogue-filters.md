# Catalogue filters

The web catalogue applies the same normalized filter predicate to sample and published vehicles before pagination. Fuel options are Electric, Hybrid, Gasoline, Diesel and Other. Body options include SUV, Sedan, Hatchback, Coupe, Convertible, Pickup, Van, Wagon and Other. Price must be below CAD 40,000 and low mileage means below 20,000 km. Missing numeric values never pass these numeric filters.

VIN decoding now persists NHTSA `BodyClass`, `FuelTypeSecondary`, and `ElectrificationLevel` in addition to the existing primary fuel and general vehicle type. The listing response uses body class rather than assuming every passenger car is a sedan. Hybrid and plug-in hybrid vehicles are recognized from electrification level or a gasoline/diesel plus electric fuel combination. Battery electric vehicles remain distinct from hybrids.

Three nullable columns (`body_class`, `fuel_type_secondary`, `electrification_level`) are added to `vin_decode_records` by the local Hibernate update profile. For managed deployments, add equivalent nullable varchar(255) columns through schema migrations. Existing decoded records without body style are refreshed on their next VIN lookup, preserving the record and avoiding duplicate vehicles. If the external lookup fails, the existing data stays available and unknown classification remains Other.

Newly published vehicles join the existing catalogue refresh automatically and use the active filters. Sample prices/specifications are illustrative; Wikimedia photos represent their respective models, with author/license links on cards and details.

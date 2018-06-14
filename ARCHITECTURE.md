# CopShop Architecture
## Architecture Diagram
![Architecture Diagram](https://i.imgur.com/3QJR5Rk.png)
Image is also available in root of repo.

## Architectural Overview
CopShop is designed to employ a 3-tier architecture. Tiers are broken down into Persistence, Logic, and Presentation. All communication between these tiers is done through intermediary classes that have corresponding interfaces. These classes are implemented as singleton instances that are created and are accessible via one root class, CopShopApp, which is initialized automatically by Android on startup.   

Communication with the database is done through Model classes, and communication with the logic layer is done through Service classes. Activities in the presentation layer call Service methods in the logic layer, which in turn call Model methods, which are wrappers for Database queries.  

Currently, users can create new listings, and view basic information about existing listings. Users can also create buyer accounts and log into them.  

## Detailed Breakdown
### Presentation
- Contains Activities, classes that contain all presentational logic and UI controls.
	- CreateAccount
 	- CreateBuyerAccount
	- CreateListing
	- ListingList
	- ListingView
	- Login
- **Utilities**
	-  Contains generic utility functions, such as for string truncation.
- **Classes**
	- Contains custom subclasses of Android and other built-in classes.
	
### Logic
- **Services**
	- Contain Interfaces defining what logic is required for different actions/activities.
	- **Stubs**
		- Classes that implement all complex 'business logic", and processing unrelated to presentation, which is defined in the interfaces above. 

### Application
- **CopShopApp**
	- Ultimate container for all Service instances and database Model instances(singletons). 
	- Data members are initialized when the app becomes active. Normally destroyed when the app enters the background - this currently destroys the database.
	- Access to this class is global.
		- Presentation layer calls methods on Service instances.
		- Logic layer calls methods on Model instances. 
		- May be split in a future iteration to further separate these boundaries.

### Persistence
- All interaction with the persistence layer is done through **"Models"**.
	- Plain-English interfaces that abstract away raw calls to the database. 
		- Instead of explicitly writing something like `database.insertRow("Listings", ...)`, one can simply write something like `listingModel.createNew(listingObject)`.
	- Provides a convenient place to pack and unpack data objects to simplify data-passing, as well as do more complex storage-related processing.
	- **Database**
		- Contains the overall database interface, as well as the implementation.
		- **MockDatabaseStub** 
			- Stub implementation for the database.
			- Triple-nested hash table.
				- Database: `TableNameString->TableContentsHashTable`
				- TableContentsHashTable: `RowIdString->RowColumnsHashTable`
				- RowColumnsHashTable: `ColumnNameString->ColumnValueString`

### Objects
- Package consisting of data-containing objects that are used across all parts of the application.
	- **AccountObject**
		- **BuyerAccountObject**
			- Contains information related to buyer accounts, such as email and password.
		- **SellerAccountObject**
			- Contains information related to seller accounts, such as email and password.
			- Sellers will have specialized data associated with them in the future, such as their address/location.
	- **ListingObject**
		- Contains information about listings, such as title, description, and who posted the listing.
	- **ListingFormValidationObject**
		- Contains validity information specific to the listing creation form.
		- Data members correspond to fields on the form. Fields containing invalid data are flagged in one of these objects and used to relay error information back to the user.
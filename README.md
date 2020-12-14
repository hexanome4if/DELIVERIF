# DELIVERIF
Desktop application to compute optimal pickup & delivery tours.
Technologies used: Java with Maven, Java FX, GraphStream (https://graphstream-project.org/)

## Context
Welcome to DELIVERIF, a desktop application to plan and optimise pickup and delivery routes for couriers.
This app can process real maps as long as they're properly formatted (see examples of different size maps of the city of Lyon for format reference in the resources folders).
A courier starts the app, they provide the file of their city map as well as the pickup and delivery orders that were assigned to them. Upon import, they can immediately visualise all the orders and details through interacting with the map. The app is there to assist them in scheduling their shift by outputting and visualising an optimal tour to complete all pickups and deliveries in minimal time. They can click ‘Compute Tour’ and wait as the app does all the computations for them. The algorithm will run indefinitely to optimise the result but the user can interrupt the computations as soon as they get a route they're satisfied with. If there are any changes to the shift that were previously unaccounted for, the app also allows them to manually edit the calculated tour through inserting and removing requests or updating the visiting order to accommodate customer pickup/delivery preferences for example, the arrival times are then updated. To ensure a more pleasant user experience, the app provides the flexibility to undo or redo manual changes.


## Installation

Download the project on GITHUB (https://github.com/hexanome4if/DELIVERIF) to install DELIVERIF.

```bash
git clone https://github.com/hexanome4if/DELIVERIF.git
```

## Usage

Use NETBEANS to "Clean & Build" the project, then "Run" the application.

Guide :

	- Click on the "Load City Map" button to load a map (need an XML file)
	- Click on the "Load Request" button, after you have already loaded a map, to load a request (need an XML file)
	- Click on the "Compute Tour" button, after you have already loaded a request, to start computing a tour
	- Click on the "Stop Search" button, when the tour is computing, to stop the algorithm and keep the current tour if you're satisfied with it. 
	- Click on the "Add request" button, after a tour is computed, to add a request and follow the instructions
	- Click on the "Delete request" button, after a tour is computed after selecting a request to delete it
	- Click on the "Swap Request" button, after a tour is computed, to swap two requests and follow the instructions
	
On the left of the application, you have a selection of useful information on the computed tour as well the pickup and delivery points.
You can interact with both the map and the list.

Keyboard shortcuts :

	- CTRL + Z = undo action
	- CTRL + Y = redo action
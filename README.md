# DELIVERIF
Desktop application to compute optimal pickup & delivery tours.
Technologies used: Java with Maven, Java FX, GraphStream (https://graphstream-project.org/)

## Installation

Download the project on GITHUB (https://github.com/hexanome4if/DELIVERIF) to install DELIVERIF.

```bash
git clone https://github.com/hexanome4if/DELIVERIF.git
```

## Usage

Use NETBEANS to "Clean & Build" the project, then "Run" the application.

Guide :

	- Click on "Load City Map" button to load a map (need a XML file)
	- Click on "Load Request" button, when you have already loaded a map, to load a request (need a XML file)
	- Click on "Compute Tour" button, when you have already loaded a request, to start computing a tour
	- Click on "Stop research" button, when the tour is computing, to stop the algorithm and keep the current tour 
	- Click on "Add request" button, when a tour is computed, to add a request and follow the instructions
	- Click on "Delete request" button, when a tour is computed and you have selected a request, to delete a request
	- Click on "Swap Request" button, when a tour is computed, to swap two requests and follow the instructions
	
On the left of the application, you have a lot of usefull informations.
You can interact with the map and the list.

Keyboard shortcuts :

	- CTRL + Z = undo action
	- CTRL + Y = redo action
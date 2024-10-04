# New Invasive Species Reporting Tool

![App Screenshot](https://github.com/thomaslui003/Invasive-Species-Reporting-Tool-/blob/main/simpleWebApp_wlui/interface.jpg)

## Overview

This web application allows users to report and track invasive species in various regions. The app features CRUD (Create, Read, Update, Delete) operations connected to a PostgreSQL database, enabling users to submit, edit, and delete reports of invasive species sightings. 

The app is built using the MVC architecture, with JSP and Servlets handling the backend logic and database interactions. The frontend is constructed using HTML, CSS, and JavaScript to provide a simple and functional user interface.

## Features

- **CRUD Operations**: Users can create, view, update, and delete reports of invasive species.
- **Search Functionality**: Users can filter reports based on species name, reporter name, or province.
- **User Authentication**: Basic user login functionality to differentiate reports based on users.
- **Selenium Testing**: Automated tests are implemented using Selenium to ensure functionality.
- **JUnit Testing**: Unit tests for backend logic with JUnit.

## Tech Stack

- **Backend**: Java (Servlets, JSP)
- **Frontend**: HTML, CSS, JavaScript
- **Database**: PostgreSQL
- **Web Server**: Apache Tomcat
- **Testing**: Selenium, JUnit
- **Version Control**: Git

## Project Structure

The project follows the **MVC (Model-View-Controller)** architecture:

- **Model**: Represents the data (invasive species reports) and the business logic.
- **View**: The user interface (JSP pages) that presents data to the user and sends user requests to the controller.
- **Controller**: The servlets that handle interactions between the view and the model.

## Screenshots

### Main Interface

Here’s a screenshot of the main interface that displays a list of reported invasive species:

![Web App Screenshot](insert-image-url-here)

### Report Form

This is the form users fill out when reporting a new invasive species:

![Report Form Screenshot](insert-image-url-here)

> **Note**: To add your own screenshots, first, upload the images to your GitHub repository, then replace the `insert-image-url-here` with the direct URLs of those images.

## How to Run

### Prerequisites

Before running the application, ensure you have the following installed:

- [Java JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Apache Tomcat](https://tomcat.apache.org/)
- [PostgreSQL](https://www.postgresql.org/)
- [Maven](https://maven.apache.org/)

### Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/invasive-species-reporting-tool.git
   cd invasive-species-reporting-tool

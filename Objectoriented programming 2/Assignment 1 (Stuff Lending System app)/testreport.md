# Stuff Lending Test Report

This document provides a comprehensive overview of the final system test results for my application.

## Test Summary

The application was tested thoroughly to ensure all functionalities work as expected. The tests covered all aspects of the application, including member creation, item management, contract establishment, and time advancement.

## Test Objectives

The main objective of the tests was to verify that all functionalities of the application work as expected and that the application adheres to the specified requirements.

## Test Environment

The tests were conducted on a local development environment with the latest version of the application code.

## Test Methodology

The tests were conducted manually by interacting with the application's user interface and verifying that each functionality works as expected.

## Test Results

### Member

- **Create**: The application successfully creates a new member with a unique name, email, and mobile phone number. A unique member ID is generated and assigned to the new member, and the day of creation is recorded.
- **Delete**: The application successfully deletes a member when requested.
- **Change Information**: The application successfully changes a member's information when requested.
- **View Information**: The application successfully displays a specific member's full information when requested.
- **List Members**: The application successfully lists all members in both a simple way (Name, email, current credits, and number of owned items) and in a verbose way (Name, email, information of all owned items including who they are currently lent to and the time period).

### Item

- **Create**: The application successfully creates a new item for a member. The item has a category (Tool, Vehicle, Game, Toy, Sport, Other), a name, a short description, the day of creation is recorded, and a cost per day to lend the item. When created, the owning member gets 100 credits.
- **Delete**: The application successfully deletes an item when requested.
- **Change Information**: The application successfully changes an item’s information when requested.
- **View Information**: The application successfully displays an item's information including the contracts for an item (historical and future).

### Contract

- **Establish Contract**: The application successfully establishes a new lending contract with a starting day, an ending day and an item. Credits are transferred according to the number of days and the price per day of the item.

### Time

- **Advance Day**: The application successfully advances the current day when requested.

## Defects Summary

No defects were found during testing. All functionalities worked as expected.

## Conclusion

The testing was successful. All functionalities of the application work as expected and adhere to the specified requirements. No defects were found during testing. I recommend proceeding with the deployment of the application.

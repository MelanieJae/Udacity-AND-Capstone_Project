This is the repository for my final capstone project for Udacity's Android Developer Nanodegree.

# App Notes

App will use typical navigation (e.g. back button, overflow menu button and navigation fragment with menu options). Overflow menu will be accessible from every screen and contain all of the same options as the menu fragment as well as a Settings item where user preferences will be stored and retrieved as needed throughout the app.

Intended User

Anyone who is pressed for time and needs to plan a memorial and burial or cremation for a loved one as well as those attending services or the burial site of a deceased family member or friend in another part of the country.

Features

● Search of obituaries

● Search for service providers (e.g. burial, cremation)

● “Things to Remember” Checklist

● Scheduling a consult for pre-planning/financing OR Create, save and share a memorial
and burial/cremation plan through phone

● Find/navigate to a loved one's memorial and/or burial site gives Google Maps directions
and, when geofencing is activated, continues right to actual plot

● Bill Pay


# Development Notes

Data persistence
User selections in Settings (including any and all plan choices) will be stored to a local DB which is accessed via a custom content provider that I will create.

Third party libraries

Google Play Services 

● Maps-for location of service and/or burial site to guide user to the location

● Analytics-track page views, possibly also tags

● Location/Context-used to change navigation format from Google Maps to locating

precise burial site or mausoleum within cemetery

# Build Notes

This project and its base project are built using the Gradle build system.

To build the project from Android Studio, follow these instructions: https://developer.android.com/studio/build/building-cmdline.html#DebugMode.

# Description

This app is a mobile adaptation of an existing website for Dignity Memorial, LLC, a network of funeral planning and site providers throughout the US. It provides quick access to the most valuable services that the existing Dignity Memorial website provides, e.g. search of obituaries, search for service providers (e.g. burial, cremation), a “Things to Remember” checklist, and creating, saving and sharing an event plan via a mobile device.

# Attributions:
ZoomOutPageTransformer class (entire class)
ScreenSlidePageAdapter(portions)
https://developer.android.com/training/animation/screen-slide.html







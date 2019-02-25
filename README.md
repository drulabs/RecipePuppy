# Recipes - CLEAN Architecture in Android

## About
This project (a relatively simple one) is an attempt to adopt CLEAN architecture in Android. It showcases usage of Android Architecture Components with RxJava and Dagger2 together to achieve the CLEAN architecture. Please note, this is an adoption of the original idea presented by Rober C. Martin in "Clean Architecture: A Craftsman's Guide to Software Structure and Design", the core principles are intact but other rules are modified to showcase working or RxJava, dagger2 and Room with RxJava and LiveData. 

## About the app
This app has 2 screens. First is Home screen, it allows users to search for a particular recipe term and displays result in a Recycler view, user can favorite a recipe and open the details age by clicking on the recipe image.
 
Second is the favorites screen, users can navigate here from the overflow menu on he home screen. All the favourited recipes are visible here. Unfavouriting a recipe removes it from the list. Favouriting the recipe saves it in the local SQLite database via ROOM, unfavouriting it, deletes it.
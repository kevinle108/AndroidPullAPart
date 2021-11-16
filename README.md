# AndroidPullAPart Car Search

## Description:
A Pull-A-Part Search App that supports multiple cars of different makes!

This Android app (built in Kotlin) was created mainly for me to have a better experience when having to search for cars a Pull-A-Part salvage yard. The issue I had with the car search on Pull-A-Part's website was that it only allows you to search multiple cars for the same make. For example, it will allow you to search for both Honda Civic and Honda Accord, but there is no way to search for both Honda Civic and Toyota Camry. Whenever I find myself going to Pull-A-Part, I often need to search cars of multiple makes. So I created this webapp that will allow users to search for cars of various makes and models and present their lot locations all in one sorted table. This makes my Pull-A-Part experience (and hopefully yours) a lot more efficient because you no longer need to pull up several different pages if you want to search for cars of multiple makes.

NOTE: At this time, the app only searches cars at Pull-A-Part's Louisville location.

## Code Louisville Android Project Requirements met:
- External API calls to Pull-A-Part's database to fetch search results
- Uses many data classes to represent Make, Model, LotItem, etc.
- Uses MVVM pattern using ViewModel & ViewBinding
- Allows user interaction via dropdowns, buttons, and dialogs
- Implements separate screens for search and results
- Uses external libraries: RetroFit & Moshi for REST API

## Know Issues:
- Orientation change causes current Model dropdown to reset to first item

## Tested Devices:
- Motorola G Stylus
- Motorola G7 Power
- Google Pixel
- Google Pixel 2 API 29 (Virtual Device)

## Special Thanks:
- Mentors Zach Tibbits and Alex Vance for teaching me Android. This app wouldn't have been possible without you!
- Code Louisville Program for always providing me great classes to further my software development skills
- https://www.pullapart.com/ for the original inspiration

## Author
Kevin Le

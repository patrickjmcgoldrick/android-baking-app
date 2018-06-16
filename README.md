# android-baking-app

Given, [example JSON](https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json)

Build an app from scratch that shows baking recipes.  Including ingrediants, steps, videos and images.

## Rubric - Make A Baking App

### Common Project Requirements

CRITERIA | MEETS SPECIFICATIONS
--- | ---
App is written solely in the Java Programming Language | App is written solely in the Java Programming Language
Submission must use stable release versions of all libraries, Gradle, and Android Studio. Debug/beta/canary versions are not acceptable. | App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### General App Usage

CRITERIA | MEETS SPECIFICATIONS
--- | ---
Display recipes | App should display recipes from provided network resource.
App Navigation | App should allow navigation between individual recipes and recipe steps.
Utilization of RecyclerView | App uses RecyclerView and can handle recipe steps that include videos or images.
App conforms to common standards found in the Android Nanodegree General Project Guidelines. | App conforms to common standards found in the Android Nanodegree General Project Guidelines.  

### Components and Libraries

CRITERIA | MEETS SPECIFICATIONS
--- | ---
Master Detail Flow and Fragments | Application uses Master Detail Flow to display recipe steps and navigation between them.
Exoplayer(MediaPlayer) to display videos | Application uses Exoplayer to display videos.
Proper utilization of video assets | Application properly initializes and releases video assets when appropriate.
Proper network asset utilization | Application should properly retrieve media assets from the provided network links. It should properly handle network requests.
UI Testing | Application makes use of Espresso to test aspects of the UI.
Third-party libraries| Application sensibly utilizes a third-party library to enhance the app's features. That could be helper library to interface with ContentProviders if you choose to store the recipes, a UI binding library to avoid writing findViewById a bunch of times, or something similar.

### Homescreen Widget

CRITERIA | MEETS SPECIFICATIONS
--- | ---
Application has a companion homescreen widget.  | Application has a companion homescreen widget.
Widget displays ingredient list for desired recipe. | Widget displays ingredient list for desired recipe.

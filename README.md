[//]: # (Image References)

[image1]: ./artifacts/phone_screenshots.png "Phone Screenshot"
[image2]: ./artifacts/tablet_opening.png "Tablet Opening Screenshot"
[image3]: ./artifacts/tablet_detail.png "Tablet Detail Screenshot"


# android-baking-app

Udacity - Android Developer Nanodegree, Project #3

Given, [example JSON](https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json)

* Build an app from scratch that shows baking recipes.  
* Show ingrediants, steps, videos, and images.  
* Adjust screen formats for both phones and tablets.

<table>
<tr align=center>
  <td> Recipe List </td><td> Detail </td><td> Recipe Steps </td>
</tr>
<tr> <td colspan=3> <img src="./artifacts/phone_screenshots.png"/> </td> </tr> 
</table>



Recipe List | Recipe Steps
--- | ---
![Tablet Baking App - Opening][image2] | ![Tablet Baking App - Detail][image3]

## Rubric - Make A Baking App

### Common Project Requirements

CRITERIA | MEETS SPECIFICATIONS
--- | ---
- [x] App is written solely in the Java Programming Language | App is written solely in the Java Programming Language
- [x] Submission must use stable release versions of all libraries, Gradle, and Android Studio. Debug/beta/canary versions are not acceptable. | App utilizes stable release versions of all libraries, Gradle, and Android Studio.

### General App Usage

CRITERIA | MEETS SPECIFICATIONS
--- | ---
- [x] Display recipes | App should display recipes from provided network resource.
- [x] App Navigation | App should allow navigation between individual recipes and recipe steps.
- [x] Utilization of RecyclerView | App uses RecyclerView and can handle recipe steps that include videos or images.
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines. | App conforms to common standards found in the Android Nanodegree General Project Guidelines.  

### Components and Libraries

CRITERIA | MEETS SPECIFICATIONS
--- | ---
- [x] Master Detail Flow and Fragments | Application uses Master Detail Flow to display recipe steps and navigation between them.
- [x] Exoplayer(MediaPlayer) to display videos | Application uses Exoplayer to display videos.
- [x] Proper utilization of video assets | Application properly initializes and releases video assets when appropriate.
- [x] Proper network asset utilization | Application should properly retrieve media assets from the provided network links. It should properly handle network requests.
- [x] UI Testing | Application makes use of Espresso to test aspects of the UI.
- [x] Third-party libraries| Application sensibly utilizes a third-party library to enhance the app's features. That could be helper library to interface with ContentProviders if you choose to store the recipes, a UI binding library to avoid writing findViewById a bunch of times, or something similar.

### Homescreen Widget

CRITERIA | MEETS SPECIFICATIONS
--- | ---
- [x] Application has a companion homescreen widget.  | Application has a companion homescreen widget.
- [x] Widget displays ingredient list for desired recipe. | Widget displays ingredient list for desired recipe.

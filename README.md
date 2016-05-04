# Retail Android app with a Google Spreadsheet as backend #
Customizable Android apk to demonstrate retail use case with Google Drive.
No complex backend, just a Google Spreadsheet stored on a Google Apps account.
Drive for Work (Google Apps unlimited) offers unlimited storage for enterprises, as well as audit, archiving and eDiscovery to all company files.
More info [here](https://apps.google.com/driveforwork/)

The apk uses this [spreadsheet as backend](https://docs.google.com/spreadsheets/d/1zQMzthur_TkahfG-8-vBWQgXJVxdT9UnhHJavRJAHhI/edit?usp=sharing), mandatory to execute the apk. 
The apk makes use of JSON download functionality of Google Spreadsheets as explained [in this video](https://www.youtube.com/watch?v=RSgMEtRl0sw), and also [prediction API](https://developers.google.com/apps-script/advanced/prediction). 

The spreadsheet contains up to three Apps Script projects, one for the prediction API, another one to download data and another one to download config parameters, like logos, titles, subtitle and background color.


## Usage

1) Compile and launch the apk

2) Open the spreadsheet backend to see the data.

3) If you want to customize content, you need to create a copy of the spreadsheet, publish the two scripts and finally change the two URLs accordingly in the code
(D4WSyncAdapter.java)

4) Click Refresh on the apk to see the changes

## Backend

The backend is a [Google Spreadsheet](https://docs.google.com/spreadsheets/d/1zQMzthur_TkahfG-8-vBWQgXJVxdT9UnhHJavRJAHhI/edit?usp=sharing), hosted on Drive for Work backend


## Dependencies

The following libraries must be included for proper compilation and execution:

```groovy  
      compile 'com.android.support:cardview-v7:23.3.0'
      compile 'com.android.support:recyclerview-v7:23.3.0'
      compile 'com.squareup.picasso:picasso:2.5.0'
      compile 'com.android.volley:volley:1.0.0'
```


## Screenshots

Main activity, details and view to send email:


<img src="https://raw.githubusercontent.com/rafaelsf80/d4wRetail/master/screenshots/main.png" alt="alt text" width="100" height="200">
<img src="https://raw.githubusercontent.com/rafaelsf80/d4wRetail/master/screenshots/details.png" alt="alt text" width="100" height="200">
<img src="https://raw.githubusercontent.com/rafaelsf80/d4wRetail/master/screenshots/survey.png" alt="alt text" width="100" height="200">

# Serverless inventory management app for SMB with a Google Spreadsheet#
Customizable Android apk to demonstrate retail use case with Google Apps.
Serverless app, just a Google Spreadsheet as backend to store the inventory.
Vision API and Prediction API are also used.

The apk uses this [spreadsheet as backend](https://docs.google.com/spreadsheets/d/1zQMzthur_TkahfG-8-vBWQgXJVxdT9UnhHJavRJAHhI/edit?usp=sharing), and makes use of JSON download functionality of Google Spreadsheets as explained [in this video](https://www.youtube.com/watch?v=RSgMEtRl0sw), together with the [Prediction API](https://developers.google.com/apps-script/advanced/prediction). 

The spreadsheet contains **three Apps Script projects**, one for the prediction API, another one to download data and another one to download config parameters, like logos, titles, subtitle and background color.

Visit how to evolve the retail industry with [Google for Work](https://apps.google.com/driveforwork/) watching the [Atmosphere Digital online event](https://atmosphere.withgoogle.com/live/atmosphere-retail-2016-april-amer).
Drive for Work (Google Apps unlimited) offers unlimited storage for enterprises, as well as audit, archiving and eDiscovery capabilities.


## Usage

1) Compile and launch the apk

2) Open the spreadsheet backend to see the data.

3) If you want to customize content, you need to create a copy of the spreadsheet, publish the two scripts and finally change the two URLs accordingly in the code
(D4WSyncAdapter.java)

4) Click Refresh on the apk to see the changes

## Serverless

No backend server, just a [Google Spreadsheet](https://docs.google.com/spreadsheets/d/1zQMzthur_TkahfG-8-vBWQgXJVxdT9UnhHJavRJAHhI/edit?usp=sharing), hosted on Drive for Work backend


## Vision API (beta)

Last release includes a vision API to detect retail content on photos. To be integrated in the workflow shortly.

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

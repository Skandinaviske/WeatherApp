# WeatherApp

This is a weather app written by Ailwyn Liang.   
一个天气App

## Environment:

minSdkVersion      21  
targetSdkVersion   30   

## Development tools:

Android Studio

## Describe:

The basic techs of this app are MVVM, Retrofit, ViewModel, DataBinding, LiveData and Room.  
The coding language of this app is Kotlin.  
使用的技术包括MVVM, Retrofit, ViewModel, DataBinding, LiveData and Room， Kotlin语言编写。

## App Display:

#### Boot page：

<img width="200" src="https://github.com/Skandinaviske/WeatherApp/blob/master/IMG/Start Pic.jpg" style="zoom: 25%;" />  

This is the boot page. It will show when you start this app. The first time( after install this app) when you open this app, it will ask you for location permission so that we can get your position to request API from remote server. After we get the weather data back, this page will transfer to Home page.



#### Home Page:
<p float="left">
	<img width="200" src="https://github.com/Skandinaviske/WeatherApp/blob/master/IMG/Home 1.jpg"/>
	<img width="200" src="https://github.com/Skandinaviske/WeatherApp/blob/master/IMG/Home 2.jpg"/>
	<img  width="200" src="https://github.com/Skandinaviske/WeatherApp/blob/master/IMG/Air Condition.jpg"/><img 
</p>
As the first page, the app will give you the weather data where you are now, which also includes the the weather and temperature per hour and those in the next 7 days.

As the second page, the app will also give you some other information and life suggestions.

We can also click the button on the home page to see the air quality as the third page. It contains 7 items about the air quality.


#### City Management:

<img width="200" src="https://github.com/Skandinaviske/WeatherApp/blob/master/IMG/City Management.jpg" alt="City Management" style="zoom:25%;" />

The city management page displays the cities which you prefer to get their weather information brief. The items' background depends on their weather type.

You can click on the FloatingActionButton to add more cities there. You can also delete them by click the button in the right-top of this page. 

What's more, you can choose the city you like to click and see the specific weather information as what the  home page has shown.  

#### City Search:

<img width="200" src="https://github.com/Skandinaviske/WeatherApp/blob/master/IMG/City Search List.jpg" alt="City Search List" style="zoom:25%;" />

Finally, this image shows you the bottomsheetDialog after you  click on the FloatingActionButton on the city management page. As you can see, you can search for the city you want and you can also choose the most popular city under the search box.

## Author:  

Ailwyn Liang

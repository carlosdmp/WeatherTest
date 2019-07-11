# WeatherApp


## Instructions:
I'm importing my own api key to the project, you can set your own api key in your gradle properties, or just change "BuildConfig.ApiKey" for your api key in the data module.

## Comments:
I'm using some latest techs to show that I'm a proactive learner that wants to be up to date about android related technologies.

## Coroutines 
Last retrofit version allows you to make the http calls as suspend functions, which allows the coroutines code to be really easy to write and efficient.

Even when I am more fluent in RxJava than in Kotlin coroutines, I have decided to use coroutines because Rx is such a heavy library for this small project. 

## Arrow
Arrow is the functional Kotlin companion library, if you are not familiar with it, these are the data types I have used for this project:

https://arrow-kt.io/docs/arrow/core/try/

https://arrow-kt.io/docs/arrow/core/either/

I have used Arrow to write more functional code, and handle all errors in a good way, avoiding long Try/Catch and different callbacks for success and error cases, without having to create these utilities by my own. In a big project I would probably just write my own data types instead.


## UseCase
My case is probably the most strange part of the code for Kotlin newcomers: What it does is:
* Switch to the IO thread dispatcher, and execute the first repository call, handling the error
~~~
withContext(io) {
Try { repo.fetchWeather(weatherRequest) }.toEither { t -> DomainError(t) }
~~~
* Map the right side of the either from the weather to the list of new coords
~~~
.flatMap { originWeather ->
Try {
CoordCalculator.getAllCoords(
Coord(
lat = originWeather.coord.lat,
lon = originWeather.coord.lon
)
~~~
* Map the coords to it's Weather info by calling the repo again, and these calls are asynchronous between them
~~~
).map { geoPoint ->
async {
DomainWeatherPoint(
geoPoint.point,
repo.fetchWeather(
WeatherRequest.CoordRequest(geoPoint.coord)
)
)
}
~~~

* Wait until all calls are done succesfully, and add the origin weather to the others. And finally just handle the error if some call failed
~~~
}.map {
it.await()
} + DomainWeatherPoint(Point.ORIGIN, originWeather)
}.toEither { t -> DomainError(t) }
~~~

## ViewModel
I'm using Architecture components for the presentation layer, like ViewModel or LiveData.
The only thing the viewmodel does is map the domain object to the display model, and notify the change to the livedata observed by the fragment

## Testing
Im using mockk to make unit tests, since it's a kotlin testing library and it has a lot of nice improves to old java tools like mockito, also, if not, mocking and testing things like objects or suspend functions would be a problem.

I have tested the more interesting class of each layer, the ViewModel for presentation, the Case for Domain, and the Api for data.

I did not created UI tests since the UI is quite simple

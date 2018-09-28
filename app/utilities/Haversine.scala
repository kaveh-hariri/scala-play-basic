package utilities



trait DistanceCalcular {
  def calculateDistanceInKilometer(userLocation: models.Location, warehouseLocation: models.Location): Int
}

class DistanceCalculatorImpl extends DistanceCalcular {

  private val AVERAGE_RADIUS_OF_EARTH_KM = 6371

  override def calculateDistanceInKilometer(userLocation: models.Location, warehouseLocation: models.Location): Int = {
    val latDistance = Math.toRadians(userLocation.lat - warehouseLocation.lat)
    val lngDistance = Math.toRadians(userLocation.lon - warehouseLocation.lon)
    val sinLat = Math.sin(latDistance / 2)
    val sinLng = Math.sin(lngDistance / 2)
    val a = sinLat * sinLat +
      (Math.cos(Math.toRadians(userLocation.lat))
        * Math.cos(Math.toRadians(warehouseLocation.lat))
        * sinLng * sinLng)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
    (AVERAGE_RADIUS_OF_EARTH_KM * c).toInt
  }
}


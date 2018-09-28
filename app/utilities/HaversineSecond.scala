package utilities

object HaversineSecond {

  def distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Float = {
    val p: Double = 0.017453292519943295
    val a = 0.5 - Math.cos((lat2 - lat1) *p) / 2 + Math.cos(lat1 * p) * Math.cos(lat2 * p) * (1 - Math.cos((lon2 - lon1) * p))/2
    (12742 * Math.asin(Math.sqrt(a))).toFloat
  }

}

package models

case class count (
                 city: String
                 , count: Int
                 )
case class top (
               sightings: Seq[count]
               )
case class Location(lat: Double, lon: Double)

case class closest(
                  sightings: Seq[sightings]
                  )
case class sightings (
                     id: Option[Int]
                     , occurred_at: Option[String]
                     , city: Option[String]
                     , state: Option[String]
                     , country: Option[String]
                     , shape: Option[String]
                     , duration: Option[Double]
                     , duration_text: Option[String]
                     , description: Option[String]
                     , reported_on: Option[String]
                     , latitude: Option[Double]
                     , longitude: Option[Double]
                     , distance: Option[Float]
                     )
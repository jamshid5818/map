package jx.pdp_dars.map_july.ui.data.models.local

class HistoryData {
    var travelName: String = ""
    var travelDist: Long = 0
    var travelAvgSpeed: Double = 0.0
    var travelTime: Long = 0

    constructor(travelName: String, travelDist: Long, travelAvgSpeed: Double, travelTime: Long) {
        this.travelName = travelName
        this.travelDist = travelDist
        this.travelAvgSpeed = travelAvgSpeed
        this.travelTime = travelTime
    }

    constructor()

}
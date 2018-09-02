class DcmStats(val trackedParticle: TrackableParticle, val dcmList: MutableList<Triple<Double, Double, Double>> = mutableListOf()) {

    companion object {
        fun fromStats(stats: List<Stats>): List<DcmStats> {
            if (stats.isEmpty()) {
                return emptyList()
            }

            val statsMap = mutableMapOf<TrackableParticle, MutableMap<Double, MutableList<Double>>>()

            stats[0].trackedParticles.forEach { particle ->
                statsMap[particle] = mutableMapOf()
                particle.positions.forEach { (time, position) ->
                    statsMap[particle]!![time] = mutableListOf(Vector.norm(position))
                }
            }

            for (i in 1 until stats.size) {
                stats[i].trackedParticles.forEach { particle ->
                    val timeMap = statsMap[particle]
                    particle.positions.forEach { (time, position) ->
                        if (!timeMap!!.contains(time)) {
                            timeMap[time] = mutableListOf()
                        }
                        timeMap[time]!!.add(Vector.norm(position))
                    }
                }
            }

            val aggregatedDcmList = mutableListOf<DcmStats>()

            statsMap.forEach { (particle, dcmList) ->
                val dcmStat = DcmStats(particle)
                dcmList.forEach { (time, dcmList) ->
                    val mean = calculateMean(dcmList)
                    val std = calculateStd(dcmList, mean)
                    dcmStat.dcmList.add(Triple(time, mean, std))
                }

                aggregatedDcmList.add(dcmStat)
            }

            return aggregatedDcmList
        }


        private fun calculateMean(dcmList: MutableList<Double>): Double {
            var acum = 0.0
            for (dcm in dcmList) {
                acum += dcm
            }

            return acum / dcmList.size
        }

        private fun calculateStd(dcmList: MutableList<Double>, mean: Double): Double {
            if (dcmList.size == 0)
                return 0.0

            var stdAcum = 0.0
            for (num in dcmList) {
                stdAcum += (num - mean) * (num - mean)
            }

            return Math.sqrt(stdAcum / (dcmList.size - 1))
        }
    }


}
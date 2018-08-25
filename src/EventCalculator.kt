object EventCalculator {

    fun calculateNewEvents(changedParticles: ArrayList<Particle>,
                           allParticles: ArrayList<Particle>,
                           borders: Borders,
                           time: Double)
            : ArrayList<Event>{
        val events = ArrayList<Event>()
        calculateNewBorderEvents(events, changedParticles, borders, time)
        calculateNewParticleEvents(events, changedParticles, allParticles, time)
        return events
    }

    private fun calculateNewParticleEvents(events: ArrayList<Event>,
                                           changedParticles: ArrayList<Particle>,
                                           allParticles: ArrayList<Particle>,
                                           time: Double) {
        changedParticles.forEach({ changedParticle ->
            allParticles.forEach({ otherParticle ->
                if(changedParticle != otherParticle){ // TODO check if kotlin != is valid in this case
                    val newEvent = Collider.collide(changedParticle, otherParticle, time)
                    newEvent?.let{events.add(newEvent)}
                }
            })
        })
    }

    private fun calculateNewBorderEvents(events: ArrayList<Event>,
                                         changedParticles: ArrayList<Particle>,
                                         borders: Borders,
                                         time: Double) {
        changedParticles.forEach({
            val newEvent = borders.nextCollision(it, time)
            newEvent?.let { events.add(newEvent) }
        })
    }


}
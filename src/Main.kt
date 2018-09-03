import java.util.*
import kotlin.collections.ArrayList

class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            // STEP 1: Create borders and all particles

            // Simulation parameters
            val seed = 0L
            val worldWidth = 0.5
            val worldHeight = 0.5
            val maxTime = 200.0
            val EPSILON = 0.00001
            val borders = Borders(worldWidth, worldHeight)


            val bigParticleRadius = 0.05
            val bigParticleMass = 100.0
            val smallParticleRadius = 0.005
            val smallParticleMass = 0.1
            val maxVelocity = 0.2

            val particleCount = 400

            val pg = ParticleGenerator(
                    worldWidth = worldWidth,
                    worldHeight = worldHeight,
                    bigParticleRadius = bigParticleRadius,
                    bigParticleMass = bigParticleMass,
                    smallParticleRadius = smallParticleRadius,
                    smallParticleMass = smallParticleMass,
                    maxVelocity = maxVelocity,
                    EPSILON = EPSILON,
                    hardCodedSeparation = 0.05,
                    seed = seed)


            val SIMULATIONS = 1
            val statsList = mutableListOf<Stats>()


            val simName = "400_base_sim"
            StatsPrinter.changeDir(simName)
            StatsPrinter.printParameters(seed, maxTime, worldWidth, worldHeight, particleCount, bigParticleRadius, bigParticleMass, smallParticleRadius, smallParticleMass, maxVelocity, SIMULATIONS,
                    "")


            // Start simulation
            // Create Stats tracking

            for(i in 1..SIMULATIONS) {
                val simStats = Stats();

                // Generate particles
                val particles = pg.generateParticles(
                            trackedSmallParticlesNum = 1,
                            nonTrackedSmallParticlesNum = particleCount - 1,
                            stats = simStats)

                statsList.add(simStats);

                if(i == 1) {
                    StatsPrinter.printFirstVelocities(particles)
                }

                val printer = ParticlePrinter(borders, simName, i != 1)

                // Time starts at zero
                var time = 0.0
                val pq = PriorityQueue<Event>()

                // STEP 2: For all particles,
                // Calculate boards collision events
                // for each particle in all particles, calculate particles collision events
                val newEvents = EventCalculator.calculateNewEvents(
                        changedParticles = particles,
                        allParticles = particles,
                        borders = borders,
                        time = time)
                pq.addAll(newEvents)

                // Main loop
                while (time < maxTime) {
                    //  Pop event
                    if (pq.isEmpty()) break
                    val currentEvent = pq.poll()
                    // Check that collision time is always bigger if not skip event
                    if (currentEvent.results.find { it.collisionNumber != it.particle.collisionCount + 1 } == null) {

                        // STEP 3: For all particle recalculate position
                        if (time > currentEvent.tc) {
                            throw IllegalStateException("New time can't be smaller than before")
                        }
                        val oldEventTime = time
                        val oldTime = printer.printIfNecessary(particles, currentEvent.tc)
                        time = currentEvent.tc
                        val deltaTime = time - oldTime
                        val deltaTimeCollision = time - oldEventTime

                        particles.forEach {
                            it.calculateNewPosition(deltaTime, time)
                            if (it.position.x < -EPSILON || it.position.x + EPSILON > worldWidth || it.position.y < -EPSILON || it.position.y > worldHeight + EPSILON) {
                                throw IllegalStateException("Particles can't be outside of bounds")
                            }
                        }

                        simStats.saveCollisionTime(deltaTimeCollision)
                        simStats.saveVelocities(particles, SIMULATIONS == 1)


                        // STEP 4: For particles affected by event recalculate velocity and collision number
                        currentEvent.results.forEach {
                            it.particle.collisionResult(it.newVelocity, currentEvent.eventType, currentEvent.tc)
                        }

                        // STEP 2: Repeat step 2 for particles affected by event,
                        // TODO change all this to not have to create a new arrayList each time but trying to keep it DRY. Or not, maximum arraySize is 2 in our scenario
                        val changedParticles = ArrayList<Particle>()
                        currentEvent.results.forEach { changedParticles.add(it.particle) }
                        val newEvents = EventCalculator.calculateNewEvents(
                                changedParticles = changedParticles,
                                allParticles = particles,
                                borders = borders,
                                time = time)
                        pq.addAll(newEvents)
                    }

                }
            }

            if(SIMULATIONS > 1)
                  StatsPrinter.printDCM(statsList)

             StatsPrinter.printCollisionTimes(statsList[0])
             StatsPrinter.printTrajectory(statsList[0])
             StatsPrinter.printVelocitiesSegment(statsList[0], maxTime * (2/3))
        }
    }
}
class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {

            // STEP 1: Create borders and all particles

            val worldWidth = 0.5
            val worldHeight = 0.5

            val borders = Borders(worldWidth, worldHeight)
            val pg = ParticleGenerator(
                    worldWidth = worldWidth,
                    worldHeight = worldHeight,
                    bigParticleRadius = 0.05,
                    bigParticleMass = 100.0,
                    smallParticleRadius = 0.005,
                    smallParticleMass = 0.1,
                    maxVelocity = 0.1)
            val particles = pg.generateParticles(20)
            print(particles) // TODO delete this print


            // STEP 2: For all particles,
                // Calculate boards collision events
                // for each particle in all particles, calculate particles collision events

            // Main while

                //  Pop event

                // STEP 3: For all particle recalculate position

                // STEP 4: For particles affected by event recalculate velocity

                // STEP 2: For particles affected by event,
                    // Calculate boards collision events
                    // for each particle in all particles, calculate particles collision events

        }
    }
}
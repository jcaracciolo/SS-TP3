import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

/* Prints stats files. */
object StatsPrinter {
    fun changeDir(name: String) {
        File("stats").mkdirs()
        File("stats/$name").mkdirs()
        dir = "stats/$name";
    }
    var dir = "";


    fun printParameters(seed: Long, maxTime: Double, worldWidth : Double,
                        worldHeight : Double,
                        particleCount: Int,
                        bigParticleRadius : Double,
                        bigParticleMass : Double,
                        smallParticleRadius : Double,
                        smallParticleMass : Double,
                        maxVelocity : Double,
                        simulations: Int,
                        comments: String) {
        try {
            val theFile = File("$dir/simulation_data.txt")
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->
                writer.write("seed : $seed \n");
                writer.write("maxTime : $maxTime \n");
                writer.write("worldWidth : $worldWidth \n");
                writer.write("worldHeight : $worldHeight \n");
                writer.write("particleCount: $particleCount\n");
                writer.write("bigParticleRadius: $bigParticleRadius\n");
                writer.write("bigParticleMass : $bigParticleMass \n");
                writer.write("smallParticleRadius: $smallParticleRadius\n");
                writer.write("smallParticleMass : $smallParticleMass \n");
                writer.write("maxVelocity : $maxVelocity \n");
                writer.write("simulations: $simulations\n");
                writer.write("comments: $comments\n");
                writer.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun printFirstVelocities(particles: Collection<Particle>) {
        try {
                val theFile = File("$dir/initialVelocities.dat")
                BufferedWriter(OutputStreamWriter(
                        FileOutputStream(theFile), "utf-8")).use { writer ->
                    particles.forEach { particle ->
                        writer.write("${Vector.norm(particle.velocity)} ")
                    }
                    writer.close()
                }
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO do something with error
        }
    }

    /**
     * For each tracked particle there is a ${ParticleName}-DCM file with squared distance to the initial position in each frame of the animation.
     */
    fun printDCM(stats: List<Stats>) {

        try {
                val dcmAggregatedStats = DcmStats.fromStats(stats)


                dcmAggregatedStats.forEach { dcmStat ->

                val theFile = File("$dir/${dcmStat.trackedParticle.name}-DCM.dat")
                BufferedWriter(OutputStreamWriter(
                        FileOutputStream(theFile), "utf-8")).use { writer ->

                    dcmStat.dcmList.forEach() { data ->
                        writer.write("${data.first} ${data.second} ${data.third}\n")
                    }
                    writer.close()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
            //TODO do something with error
        }
    }


    fun printTrajectory(stats: Stats) {

        try {
            stats.trackedParticles.forEach  { particle ->

                val theFile = File("$dir/${particle.name}-trajectory.dat")
                BufferedWriter(OutputStreamWriter(
                        FileOutputStream(theFile), "utf-8")).use { writer ->

                    particle.positions.forEach() { (_, position) ->
                        writer.write("${position.x} ${position.y}\n")
                    }
                    writer.close()
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
            //TODO do something with error
        }
    }

    /* deltaTimes.out file is a single line of space separated valued (ssv) with the delta time between two collisions */
    fun printCollisionTimes(stats: Stats) {
        try {
            val theFile = File("$dir/deltaTimes.out")
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->
                stats.collisionTimes.forEach {
                    writer.write("${it}\n")
                }
                writer.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /* lastVelocities.dat is a file that contains the velocities of each particle during the simulation:
    *      -The first line contains N, the number of periods between collisions considered
    *      (A period is not counted if it started before the start of the last third of the simulation)
    *      -Next you have 2*N lines. Each pair of lines is composed by:
    *          -A line containing deltaT, the delta time for that period
    *          -A line with the velocities of all the particles in that period */
    fun printVelocitiesSegment(stats: Stats, cutTime: Double) {
        try {
            val theFile = File("$dir/lastVelocities.dat")
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->
                var iterations = 0
                var timeAccum = 0.0
                while (timeAccum <= cutTime) {
                    timeAccum += stats.collisionTimes[iterations++]
                }
                writer.write("${stats.velocities.size - iterations}\n")
                //                writer.write((timeAccum + collisionTimes[iterations-1] - totalTime * 2 / 3).toString() + "\n")
                //                velocities[iterations-1].forEach(){
                //                    writer.write(it.toString() + " ")
                //                }
                //                writer.write("\n")
                for (i in iterations until stats.velocities.size) {
                    writer.write("${stats.collisionTimes[i]}\n")
                    stats.velocities[i].forEach {
                        writer.write("$it ")
                    }
                    writer.write("\n")
                }
                writer.close()
            }
        } catch (e: Exception) {
        }

    }
}
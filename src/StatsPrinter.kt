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

    /**
     * For each tracked particle there is a ${ParticleName}-DCM file with squared distance to the initial position in each frame of the animation.
     */
    fun printDCM(stats: Stats) {
        try {
                stats.trackedParticles.forEach { trackedParticle ->

                val theFile = File("$dir/${trackedParticle.name}-DCM.dat")
                BufferedWriter(OutputStreamWriter(
                        FileOutputStream(theFile), "utf-8")).use { writer ->

                    trackedParticle.positions.forEach() { (timestamp, currentPosition) ->
                        val norm = Vector.norm(currentPosition - trackedParticle.initialPosition)
                        writer.write("$timestamp ${norm * norm}\n")
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
                    writer.write("${it} ")
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
                writer.write((stats.velocities.size - iterations).toString() + "\n")
                //                writer.write((timeAccum + collisionTimes[iterations-1] - totalTime * 2 / 3).toString() + "\n")
                //                velocities[iterations-1].forEach(){
                //                    writer.write(it.toString() + " ")
                //                }
                //                writer.write("\n")
                for (i in iterations..(stats.velocities.size - 1)) {
                    writer.write(stats.collisionTimes[i].toString() + "\n")
                    stats.velocities[i].forEach {
                        writer.write(it.toString() + " ")
                    }
                    writer.write("\n")
                }
                writer.close()
            }
        } catch (e: Exception) {
        }

    }
}
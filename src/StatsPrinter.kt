import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

object StatsPrinter {

    val collisionTimes = ArrayList<Double>()
    val velocities = ArrayList<ArrayList<Double>>()
    val trackedParticles = ArrayList<TrackableParticle>()


    fun saveCollisionTime(deltaTimeCollision: Double) {
        collisionTimes.add(deltaTimeCollision)

    }

    fun saveVelocities(particles: ArrayList<Particle>) {
        val speeds = ArrayList<Double>()
        particles.forEach{
            speeds.add(it.getSpeed())
        }
        velocities.add(speeds)
    }

    fun addTrackedParticle(trackableParticle: TrackableParticle) {
        trackedParticles.add(trackableParticle)
    }

    /**
     * Prints stats files.
     * deltaTime file is a single line of space separated valued (ssv) with the delta tiem between two collisions
     * For each tracked particle there is a ${ParticleName}-DCM file with squared distance to the initial position in each frame of the animation.
     * velocitiesThird is a file that contains the velocities of each particle during the simulation:
     *      -The first line contains N, the number of periods between collisions considered
     *      (A period is not counted if it started before the start of the last third of the simulation)
     *      -Next you have 2*N lines. Each pair of lines is composed by:
     *          -A line containing deltaT, the delta time for that period
     *          -A line with the velocities of all the particles in that period
     */
    fun printStats(totalTime: Double, filename: String) {
        File("stats").mkdirs()
        File("stats/" + filename).mkdirs()
        try {
            var theFile = File("stats/"+filename+"/deltaTime-" + filename)
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->
                collisionTimes.forEach {
                    writer.write("${it} ")
                }
                writer.close()
            }

            theFile = File("stats/"+filename+"/velocitiesThird-" + filename)
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->

                var iterations = 0
                var timeAccum = 0.0
                while(timeAccum <= totalTime * 2 / 3){
                    timeAccum += collisionTimes[iterations++]
                }
                writer.write((velocities.size-iterations).toString() + "\n")
//                writer.write((timeAccum + collisionTimes[iterations-1] - totalTime * 2 / 3).toString() + "\n")
//                velocities[iterations-1].forEach(){
//                    writer.write(it.toString() + " ")
//                }
//                writer.write("\n")
                for (i in iterations..(velocities.size-1)){
                    writer.write(collisionTimes[i].toString()+"\n")
                    velocities[i].forEach{
                        writer.write(it.toString() + " ")
                    }
                    writer.write("\n")
                }
                writer.close()
            }

            trackedParticles.forEach{ trackedParticle ->
                theFile = File("stats/"+filename+"/"+ trackedParticle.name+"-DCM-" + filename)
                BufferedWriter(OutputStreamWriter(
                        FileOutputStream(theFile), "utf-8")).use { writer ->

                    trackedParticle.positions.forEach(){ currentPosition ->
                        val norm = Vector.norm(currentPosition-trackedParticle.initialPosition)
                        writer.write((norm*norm).toString()+" ")
                    }
                    writer.close()
                }

            }



            } catch (e: Exception) {
            e.printStackTrace()
            //TODO do something with error
        }
    }

}
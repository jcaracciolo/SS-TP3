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

    fun printStats(totalTime: Double, filename: String) {
        File("stats").mkdirs()
        File("stats/" + filename).mkdirs()
        try {
            var theFile = File("stats/"+filename+"/deltaTime-" + filename)
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->
                collisionTimes.forEach {
                    writer.write("${it}\n")
                }
                writer.close()
            }

            theFile = File("stats/"+filename+"/velocitiesThird-" + filename)
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->

                var iterations = 0
                var timeAccum = 0.0
                collisionTimes.forEach{
                    if(timeAccum + it <= totalTime * 2 / 3){
                        timeAccum += it
                    }
                    iterations++
                }
                writer.write((velocities.size-(iterations-1)).toString() + "\n")
                writer.write((timeAccum + collisionTimes[iterations-1] - totalTime * 2 / 3).toString() + "\n")
                velocities[iterations-1].forEach(){
                    writer.write(it.toString() + " ")
                }
                writer.write("\n")

                for (i in iterations..(velocities.size-1)){
                    writer.write(collisionTimes.toString())
                    velocities[i].forEach(){
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
                        writer.write((Vector.norm(currentPosition-trackedParticle.initialPosition)).toString()+" ")
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
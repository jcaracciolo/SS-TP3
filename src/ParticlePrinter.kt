import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter

class ParticlePrinter(val borders: Borders, val supressOutput: Boolean = false) {

    var time = 0.0
    var fps = 1/15.0
    var lastFrame = 0
    var nextFrameTime = 0.0

    fun printIfNecessary(particles: Collection<Particle>, nextEventTime: Double): Double {
        if(lastFrame == 0) {
            moveParticlesAndPrint(particles)
        }

        while(nextFrameTime < nextEventTime) {
            moveParticlesAndPrint(particles)
        }

        val previousTime = time
        time = nextEventTime
        return previousTime
    }

    private fun moveParticlesAndPrint(particles: Collection<Particle>) {
        particles.forEach {
            it.calculateNewPosition(nextFrameTime - time, nextFrameTime, true)
        }
        time = nextFrameTime
        lastFrame++
        nextFrameTime += fps

        if(!supressOutput) {
            printOvito(particles)
        }
    }

    private fun printOvito(particles: Collection<Particle>) {

        val extraLines = 4

        println("Printing Frame $lastFrame at time $time")
        File("ovito").mkdirs()
        try {
            val theFile = File("ovito/particles" + lastFrame)
            BufferedWriter(OutputStreamWriter(
                    FileOutputStream(theFile), "utf-8")).use { writer ->

                writer.write("${particles.size + extraLines}\n")
                writer.write("\n")
                particles.forEach {
                    writer.write(
                            "${it.id}\t${it.position.x}\t${it.position.y}\t0\t${it.radius}\n"
                    )
                }


                val particleBorderSize = 0.00000001
                writer.write("${100000}\t${0}\t${0}\t0\t$particleBorderSize\n")
                writer.write("${100001}\t${0}\t${borders.worldHeight}\t0\t$particleBorderSize\n")
                writer.write("${100002}\t${borders.worldWidth}\t${0}\t0\t$particleBorderSize\n")
                writer.write("${100003}\t${borders.worldWidth}\t${borders.worldHeight}\t0\t$particleBorderSize\n")
                writer.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            //TODO do something with error
        }
    }
}
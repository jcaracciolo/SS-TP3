import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BordersTest {

    private val borders = Borders(10.0, 20.0)

    @Test
    fun positiveHorizontalCollisionTest() {
        val particle = Particle(1, Vector(3.0, 10.0), Vector(2.0, 0.0), 1.0, 1.0)
        val collision = borders.nextCollision(particle, 0.0)

        Assertions.assertNotNull(collision)
        Assertions.assertEquals(6.0/2.0, collision?.tc)
        Assertions.assertEquals(1, collision?.results!!.size)
        Assertions.assertEquals(-2.0, collision?.results!![0].newVelocity.x)
        Assertions.assertEquals(0.0, collision?.results!![0].newVelocity.y)
    }

    @Test
    fun negativeHorizontalCollisionTest() {
        val particle = Particle(1, Vector(7.0, 10.0), Vector(-2.0, 0.0), 1.0, 1.0)
        val collision = borders.nextCollision(particle, 0.0)

        Assertions.assertNotNull(collision)
        Assertions.assertEquals(6.0/2.0, collision?.tc)
        Assertions.assertEquals(1, collision?.results!!.size)
        Assertions.assertEquals(2.0, collision?.results!![0].newVelocity.x)
        Assertions.assertEquals(0.0, collision?.results!![0].newVelocity.y)
    }


    @Test
    fun positiveVerticalCollisionTest() {
        val particle = Particle(1, Vector(5.0, 10.0), Vector(0.0, 4.0), 1.0, 2.0)
        val collision = borders.nextCollision(particle, 0.0)

        Assertions.assertNotNull(collision)
        Assertions.assertEquals(8.0/4.0, collision?.tc)
        Assertions.assertEquals(1, collision?.results!!.size)
        Assertions.assertEquals(0.0, collision?.results!![0].newVelocity.x)
        Assertions.assertEquals(-4.0, collision?.results!![0].newVelocity.y)
    }


    @Test
    fun negativeVerticalCollisionTest() {
        val particle = Particle(1, Vector(5.0, 10.0), Vector(0.0, -6.0), 1.0, 2.0)
        val collision = borders.nextCollision(particle, 0.0)

        Assertions.assertNotNull(collision)
        Assertions.assertEquals(8.0 / 6.0, collision?.tc)
        Assertions.assertEquals(1, collision?.results!!.size)
        Assertions.assertEquals(0.0, collision?.results!![0].newVelocity.x)
        Assertions.assertEquals(6.0, collision?.results!![0].newVelocity.y)
    }


    @Test
    fun diagonalCollisionTest() {
        val particle = Particle(1, Vector(5.0, 10.0), Vector(2.0, -8.0), 1.0, 2.0)
        val collision = borders.nextCollision(particle, 0.0)

        Assertions.assertNotNull(collision)
        Assertions.assertEquals(8.0 / 8.0, collision?.tc)
        Assertions.assertEquals(1, collision?.results!!.size)
        Assertions.assertEquals(2.0, collision?.results!![0].newVelocity.x)
        Assertions.assertEquals(8.0, collision?.results!![0].newVelocity.y)
    }

    @Test
    fun stillCollision() {
        val particle = Particle(1, Vector(5.0, 10.0), Vector(0.0, 0.0), 1.0, 2.0)
        val collision = borders.nextCollision(particle, 0.0)

        Assertions.assertNull(collision)
    }

}
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ColliderTest {

    @Test
    fun horizontalCollisionTest() {
        val particle1 = Particle(1, Vector(3.0, 3.0), Vector(2.0, 0.0), 1.0, 1.0)
        val particle2 = Particle(2, Vector(17.0, 3.0), Vector(-2.0, 0.0), 1.0, 1.0)

        val collision = Collider.collide(particle1, particle2, 0.0)
        Assertions.assertNotNull(collision)
        Assertions.assertEquals(3.0, collision?.tc)
        Assertions.assertEquals(2, collision?.results!!.size)
        for (result in collision?.results) {
            if (result.particle.id == 1) {
                Assertions.assertEquals(-2.0, result.newVelocity.x)
                Assertions.assertEquals(0.0, result.newVelocity.y)
            } else {
                Assertions.assertEquals(2.0, result.newVelocity.x)
                Assertions.assertEquals(0.0, result.newVelocity.y)
            }
        }
    }

}
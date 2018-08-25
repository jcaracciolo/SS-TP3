data class Vector(val x: Double, val y: Double) {
    companion object {
        fun dot(v1 : Vector, v2: Vector) : Double {
            return v1 * v2
        }

        fun norm(v: Vector) : Double {
            return dot(v, v)
        }

        fun delta(v1: Vector, v2: Vector) : Vector {
            return v2 - v1
        }
    }

    operator fun times(v: Vector) : Double {
        return x * v.x + y * v.y
    }

    operator fun minus(v: Vector) : Vector {
        return Vector(x - v.x, y - v.y)
    }

    operator fun plus(v: Vector) : Vector {
        return Vector(v.x + x, v.y + y)
    }

    fun scaledBy(d: Double): Vector {
        return Vector(d * x, d*y)
    }
}
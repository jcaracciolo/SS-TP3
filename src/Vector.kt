data class Vector(val x: Double, val y: Double) {
    companion object {
        fun dot(v1 : Vector, v2: Vector) : Double {
            return v1.x * v2.x + v1.y + v2.y
        }

        fun norm(v: Vector) : Double {
            return dot(v, v)
        }

        fun delta(v1: Vector, v2: Vector) : Vector {
            return Vector(v2.x - v1.x, v2.y - v1.y)
        }
    }
}
package yu17

import yu17.Project

data class Path(val from: Project.Type, val to: Project.Type) {
    override fun toString(): String {
        return "$from to $to"
    }
}
package com.dali.schedulingengineers.utils

import com.dali.schedulingengineers.model.models.Engineer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

typealias EngineersScheduleCallback = (Boolean, Array<Array<Engineer?>>?) -> Unit

class ScheduleFinder(
    val maxShiftsPerDayForEngineer: Int = 1,
    val numOfDays: Int = 14,
    val numOfShiftsPerDay: Int = 3,
    val engineersList: List<Engineer>,
    val callback: EngineersScheduleCallback
) {
    var shiftsSchedule = arrayOf<Array<Int>>()

    init {
        for (i in 0 until numOfDays) {
            var day = arrayOf<Int>()
            for (j in 0 until numOfShiftsPerDay)
                day += -1
            shiftsSchedule += day
        }
    }

    private fun assginEngineerToShiftInDay(engineer: Engineer, day: Int, shift: Int) {
        shiftsSchedule[day][shift] = engineer.id
    }

    private fun unassginShiftInDay(day: Int, shift: Int) {
        shiftsSchedule[day][shift] = -1
    }

    private fun empShiftsPerDayConstraintSatisfied(engineer: Engineer, day: Int): Boolean {
        var shiftsForEngineerPerDay = 0
        for (i in 0 until numOfShiftsPerDay)
            if (shiftsSchedule[day][i] == engineer.id)
                shiftsForEngineerPerDay++
        return shiftsForEngineerPerDay < maxShiftsPerDayForEngineer
    }

    private fun empShiftsPerConsecutiveDaysSatisfied(engineer: Engineer, day: Int): Boolean {
        if (day == 0) return true
        // engineer shouldn't have shift in the previous day
        return shiftsSchedule[day - 1][0] != engineer.id && shiftsSchedule[day - 1][1] != engineer.id
    }

    private fun canAssignEmpToShiftInDay(engineer: Engineer, day: Int, shift: Int): Boolean {
        val constraint1 = empShiftsPerDayConstraintSatisfied(engineer, day)
        if (!constraint1) // shouldn't look for other constraints anymore!
            return false

        val constraint2 = empShiftsPerConsecutiveDaysSatisfied(engineer, day)
        if (!constraint2) // shouldn't look for other constraints anymore!
            return false

        return true
    }

    private fun finishedAssigning(): Boolean {
        for (day in 0 until numOfDays) {
            for (shift in 0 until numOfShiftsPerDay) {
                if (shiftsSchedule[day][shift] == -1) return false
            }
        }
        return true
    }

    private fun completedSolution(): Boolean {
        val empShiftsCount = hashMapOf<Int, Int>()
        for (element in engineersList)
            empShiftsCount[element.id] = 0

        for (day in 0 until numOfDays) {
            for (shift in 0 until numOfShiftsPerDay) {
                val empAssigned = shiftsSchedule[day][shift]
                val assignmentCount = empShiftsCount[empAssigned] ?: 0
                empShiftsCount[empAssigned] = assignmentCount + 1
            }
        }
        for (element in empShiftsCount.values)
            if (element < 2)
                return false
        return true
    }

    suspend private fun tryAssign(engineerIndex: Int, day: Int, shift: Int): Boolean {

        if (finishedAssigning()) {
            return completedSolution()
        }
        val engineer = engineersList[engineerIndex]
        if (canAssignEmpToShiftInDay(engineer, day, shift)) {
            assginEngineerToShiftInDay(engineer, day, shift)
            var nextShift = shift + 1
            val nextDay = if (nextShift == numOfShiftsPerDay) {
                nextShift = 0
                day + 1
            } else {
                day
            }
            for (i in engineersList.indices) {
                val nextEmpIndex =
                    if (engineerIndex + i == engineersList.size) 0 else engineerIndex + i
                if (tryAssign(nextEmpIndex, nextDay, nextShift))
                    return true
            }
            unassginShiftInDay(day, shift)
            return false
        }
        return false
    }

    fun mapScheduleToNames(): Array<Array<Engineer?>> {
        var shiftsScheduleWithNames = arrayOf<Array<Engineer?>>()

        for (mDay in 0 until numOfDays) {
            var day = arrayOf<Engineer?>()
            for (shift in 0 until numOfShiftsPerDay) {
                val engineer = engineersList.find { it.id == shiftsSchedule[mDay][shift] }
                day += engineer
            }
            shiftsScheduleWithNames += day
        }
        return shiftsScheduleWithNames
    }

    fun findSchedule() {
        CoroutineScope(Dispatchers.Default).launch {
            val result = tryAssign(0, 0, 0)
            withContext(Dispatchers.Main) {
                if (result) {
                    callback(true, mapScheduleToNames())
                } else {
                    callback(false, null)
                }
            }
        }
    }
}

package com.example.nurseflowd1.screens.shiftreport

sealed class VitalSuffixType {
  object None : VitalSuffixType()
  object Bp : VitalSuffixType()
  object temp : VitalSuffixType()
  object Hr : VitalSuffixType()
  object OxyRate : VitalSuffixType()
  object Respirate : VitalSuffixType()
}
@file:Suppress("DEPRECATION")

package com.example.nurseflowd1.screens.shiftreport

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import com.example.nurseflowd1.AppVM
import com.example.nurseflowd1.datamodels.P_ReportInfo
import com.example.nurseflowd1.datamodels.P_ReportMedicalInfo
import com.example.nurseflowd1.datamodels.PatientShiftReport
import com.example.nurseflowd1.datamodels.PatientVitals
import com.example.nurseflowd1.screens.AppBarColorState
import com.example.nurseflowd1.screens.AppBarTitleState
import com.example.nurseflowd1.screens.BottomBarState
import com.example.nurseflowd1.screens.NavigationIconState
import com.example.nurseflowd1.screens.nurseauth.SignupFeildsSecond
import com.example.nurseflowd1.screens.nurseauth.SingupFeilds
import com.example.nurseflowd1.screens.nurseauth.SupportTextState
import com.example.nurseflowd1.ui.theme.AppBg
import com.example.nurseflowd1.ui.theme.Bodyfont
import com.example.nurseflowd1.ui.theme.HTextClr
import com.example.nurseflowd1.ui.theme.Headingfont
import com.example.nurseflowd1.ui.theme.SecClr
import com.example.nurseflowd1.ui.theme.panelcolor
import com.itextpdf.kernel.pdf.PdfName.Border
import com.itextpdf.layout.Document
import com.itextpdf.layout.borders.Border
import com.itextpdf.layout.borders.Border3D
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.element.Text
import com.itextpdf.layout.property.TextAlignment
import kotlinx.datetime.LocalTime
import network.chaintech.kmp_date_time_picker.utils.now
import java.io.File
import java.util.Calendar
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportPage(modifier: Modifier, navcontroller : NavController, viewmodel : AppVM) {
    viewmodel.ChangeBottomBarState(barstate = BottomBarState.NoBottomBar)
    viewmodel.ChangeTopBarState(
        barstate = AppBarTitleState.DisplayTitle("Patient Shift Report"),
        colorState = AppBarColorState.DefaultColors,
        iconState = NavigationIconState.DefaultBack
    )

    // PATIENT INFO
    val user_patientname = remember { mutableStateOf("") } ; val user_patientname_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_patientid = remember { mutableStateOf("") } ; val user_patientid_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_gender = remember { mutableStateOf("") } ; val user_gender_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_wardno = remember { mutableStateOf("") } ; val user_wardno_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_dob = remember { mutableStateOf("") } ; val user_dob_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }

    // Shift Details
    val Shift_startime = remember { mutableStateOf("") } ; val shift_startime_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val Shift_endtime = remember { mutableStateOf("") } ; val shift_endtime_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val Shift_date = remember { mutableStateOf("") } ; val shift_date_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }




    // PATIENT MEDICALS
    val user_doctorname = remember { mutableStateOf("") } ; val user_doctorname_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_diagnosis  = remember { mutableStateOf("") } ; val user_diagnosis_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_medicalhistory  = remember { mutableStateOf("") } ; val user_medicalhistory_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_allergies  = remember { mutableStateOf("") } ; val user_allergies_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_painassesment = remember { mutableStateOf("") } ; val user_painassesment_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_VitalsList = remember { mutableStateOf(emptyList<PatientVitals>())}
    // PATIENT VITALS
    val user_vitalstime = remember { mutableStateOf("") } ;  val user_vitals_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_bp = remember { mutableStateOf("") } ; val user_bp_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_heartrate = remember { mutableStateOf("") } ; val user_heartrate_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_temp = remember { mutableStateOf("") } ; val user_temp_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_oxygen = remember { mutableStateOf("") } ; val user_oxygen_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_respirate = remember { mutableStateOf("") } ; val user_respirate_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }




    // REST REPORT FIELDS
    val user_pre_operative_order = remember { mutableStateOf("") } ;  val user_pre_operative_order_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_post_operative_ordre = remember { mutableStateOf("") } ; val user_post_operative_ordre_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_procedure_status = remember { mutableStateOf("") } ; val user_procedure_status_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_stat_medication = remember { mutableStateOf("") } ; val user_stat_medication_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_doc_verbal_order_name = remember { mutableStateOf("") } ; val user_doc_verbal_order_name_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }
    val user_nurse_remarks = remember { mutableStateOf("") } ; val user_nurse_remarks_state : MutableState<SupportTextState> = remember { mutableStateOf(
        SupportTextState.ideal) }


    val ShowTimeDialog = remember { mutableStateOf(false) }
    val timepicktype : MutableState<TimePickTypeState> = remember { mutableStateOf(TimePickTypeState.None) }
    Box(modifier = modifier.fillMaxSize()) {

        Column(modifier = Modifier.background(AppBg).fillMaxSize() , horizontalAlignment = Alignment.CenterHorizontally) {

            Column(modifier = Modifier.fillMaxHeight().fillMaxWidth(0.95f).background(AppBg)
                .padding(6.dp)
                .verticalScroll(rememberScrollState())  ){

                SingupFeilds(label = "Patient Name",
                    textstate = user_patientname,
                    placeholdertext = "Enter Patient name" ,
                    supportextstate = user_patientname_state,
                )
                SingupFeilds(label = "Patient Id",
                    textstate = user_patientid,
                    placeholdertext = "Enter Patient id" ,
                    supportextstate = user_patientid_state,
                )
                val context = LocalContext.current
                val genderdropExpanded =  remember {   mutableStateOf(false) }
                val genderOptions = listOf<String>( "Male" , "Female" , "Non-Binary")
                val selectedgender = remember { mutableStateOf("Female") }
                user_gender.value = selectedgender.value
                // Patient // Shift  Info Row
                Column {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)){


                        // Drop Down Gender Picker
                        Box(modifier = Modifier.weight(1f) ) {
                            ExposedDropdownMenuBox(expanded = genderdropExpanded.value , onExpandedChange = { genderdropExpanded.value = it}){
                                TextField(value = selectedgender.value , onValueChange = {} , textStyle = TextStyle(fontSize = 12.sp , color = Color.Black , fontFamily = Bodyfont),
                                    trailingIcon = {
                                        Icon(imageVector = Icons.Default.ArrowDropDown  , contentDescription = "dropdownarrow" , tint = Color.DarkGray)
                                    },
                                    modifier = Modifier.menuAnchor().border(1.dp ,Color.Black, shape = RoundedCornerShape(4.dp)),
                                    colors = TextFieldDefaults.colors(
                                        unfocusedContainerColor = AppBg,
                                        focusedContainerColor = AppBg,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent
                                    ),
                                    readOnly = true
                                )
                                ExposedDropdownMenu(expanded = genderdropExpanded.value , onDismissRequest = { genderdropExpanded.value = false },
                                    containerColor = Color.Black.copy(alpha = 0.7f),
                                    border = BorderStroke(color = Color.Black, width = 1.dp)
                                ){
                                    genderOptions.forEach { option ->
                                        DropdownMenuItem( text = { Text("$option" , style = TextStyle(fontSize = 12.sp , color = Color.White)) } , onClick = {
                                            selectedgender.value = option
                                        })
                                    }
                                }
                            }
                        }

                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_wardno,
                            placeholder = "Ward no",
                            supporttextstate = user_wardno_state,
                            isnumeric = false
                        )
                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_dob,
                            placeholder = "DOB: 12/12/2025",
                            supporttextstate = user_dob_state,
                            isnumeric = false
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)){

                        Button(onClick = {  ShowTimeDialog.value = true
                            timepicktype.value = TimePickTypeState.StartTime
                            Toast.makeText(context , "CLicked" , Toast.LENGTH_LONG).show() },
                            modifier = Modifier.weight(1f).height(56.5.dp ).border(1.dp , Color.Black, shape = RoundedCornerShape(4.dp)) ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBg
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ){
                            if(Shift_startime.value.isNullOrBlank())
                            Text("Shift Start Time" , fontSize = 12.sp, fontFamily = Bodyfont , color = Color.Gray)
                            else Text("${Shift_startime.value}" , fontSize = 12.sp , fontFamily = Bodyfont , color = Color.Black)
                        }
                        Button(onClick = {  ShowTimeDialog.value = true
                            timepicktype.value = TimePickTypeState.EndTIme
                            Toast.makeText(context , "CLicked" , Toast.LENGTH_LONG).show() },
                            modifier = Modifier.weight(1f).height(56.5.dp ).border(1.dp , Color.Black, shape = RoundedCornerShape(4.dp)) ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBg
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ){
                            if(Shift_endtime.value.isNullOrBlank())
                            Text("Shift End Time" , fontSize = 12.sp, fontFamily = Bodyfont , color = Color.Gray)
                            else Text("${Shift_endtime.value}" , fontSize = 12.sp , fontFamily = Bodyfont , color = Color.Black)
                        }


                        SignupFeildsSecond(modifier = Modifier.weight(1f),
                            textstate = Shift_date,
                            placeholder = "Date: 12/12/2025",
                            supporttextstate = shift_date_state,
                            isnumeric = false
                        )

                    }
                }


                // Patient Medical Info
                SingupFeilds(label = "Doctor",
                    textstate = user_doctorname,
                    placeholdertext = "Enter Doctor name",
                    supportextstate = user_doctorname_state
                )
                SingupFeilds(label = "Diagnosis",
                    textstate = user_diagnosis,
                    placeholdertext = "Enter Diagnosed Condition",
                    supportextstate = user_diagnosis_state
                )
                SingupFeilds(label = "Medical History",
                    textstate = user_medicalhistory,
                    placeholdertext = "Enter patient's medical history ",
                    supportextstate = user_medicalhistory_state
                )
                SingupFeilds(label = "Allergies",
                    textstate = user_allergies,
                    placeholdertext = "Enter patient's medical history ",
                    supportextstate = user_allergies_state
                )
                SingupFeilds(label = "Pain Assessment",
                    textstate = user_painassesment,
                    placeholdertext = "Enter patient's pain levels  ",
                    supportextstate = user_painassesment_state
                )

                Text( text = "Vitals" ,modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp), fontSize = 15.sp , fontFamily = Headingfont , color = panelcolor , textAlign = TextAlign.Center)
                // Vitals Column
                Column {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)){

                        Button(onClick = {  ShowTimeDialog.value = true
                            timepicktype.value = TimePickTypeState.VitalsRecordTime
                            Toast.makeText(context , "CLicked" , Toast.LENGTH_LONG).show() },
                            modifier = Modifier.weight(1f).height(56.5.dp ).border(1.dp , Color.Black, shape = RoundedCornerShape(4.dp)) ,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AppBg
                            ),
                            contentPadding = PaddingValues(0.dp)
                        ){
                            if(user_vitalstime.value.isNullOrBlank())
                                Text("Record Time" , fontSize = 12.sp, fontFamily = Bodyfont , color = Color.Gray)
                            else Text("${user_vitalstime.value}" , fontSize = 12.sp , fontFamily = Bodyfont , color = Color.Black)
                        }

                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_bp,
                            placeholder = "BP",
                            supporttextstate = user_bp_state,
                            isnumeric = true,
                            SuffixType = VitalSuffixType.Bp
                        )
                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_temp,
                            placeholder = "Temperature",
                            supporttextstate = user_temp_state,
                            isnumeric = true,
                            SuffixType = VitalSuffixType.temp
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)){
                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_heartrate,
                            placeholder = "HR",
                            supporttextstate = user_heartrate_state,
                            isnumeric = true,
                            SuffixType = VitalSuffixType.Hr
                        )
                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_oxygen,
                            placeholder = "SpO2",
                            supporttextstate = user_oxygen_state,
                            isnumeric = true,
                            SuffixType = VitalSuffixType.OxyRate
                        )
                        SignupFeildsSecond(modifier = Modifier.weight(1f) ,
                            textstate = user_respirate,
                            placeholder = "RR",
                            supporttextstate = user_respirate_state,
                            isnumeric = true,
                            SuffixType = VitalSuffixType.Respirate
                        )
                    }
                }

                // Report Fields
                SingupFeilds(label = "Pre-Operative Order",
                    textstate = user_pre_operative_order,
                    placeholdertext = "Enter pre operation orders",
                    supportextstate = user_pre_operative_order_state
                )
                SingupFeilds(label = "Post Operative Order",
                    textstate = user_post_operative_ordre,
                    placeholdertext = "Enter post operation order",
                    supportextstate = user_post_operative_ordre_state
                )
                SingupFeilds(label = "Procedure Status",
                    textstate = user_procedure_status,
                    placeholdertext = "Enter ongoing procedure status ",
                    supportextstate = user_procedure_status_state
                )
                SingupFeilds(label = "Stat Medication",
                    textstate = user_stat_medication,
                    placeholdertext = "Enter patient's immediate medications",
                    supportextstate = user_stat_medication_state
                )
                SingupFeilds(label = "Verbal Order",
                    textstate = user_doc_verbal_order_name,
                    placeholdertext = "Enter doctor's verbal orders with doctors name",
                    supportextstate = user_doc_verbal_order_name_state
                )

                SingupFeilds(label = "Nurse Remarks",
                    textstate = user_nurse_remarks,
                    placeholdertext = "Enter Nurse Remarks ",
                    supportextstate = user_nurse_remarks_state
                )
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.Center) {
                    val context = LocalContext.current
                    fun checkField(feildvalue : MutableState<String>, feildstate : MutableState<SupportTextState>) : Boolean {
                        if(feildvalue.value.isNullOrBlank()) {
                            feildstate.value = SupportTextState.empty("Required*")
                            return false
                        }
                        else {
                            feildstate.value = SupportTextState.ideal
                            return true
                        }
                    }
                    fun ValidateForm()  {

                        var isValid = true

                        isValid = checkField(user_patientname,user_patientname_state)

                        // Patient Info Details
                        isValid = checkField(user_patientname, user_patientname_state) && isValid
                        isValid = checkField(user_patientid, user_patientid_state) && isValid
                        isValid = checkField(user_gender, user_gender_state) && isValid
                        isValid = checkField(user_wardno, user_wardno_state) && isValid
                        isValid = checkField(user_dob, user_dob_state) && isValid

                        // Shift Validation
                        isValid = checkField(Shift_startime, shift_startime_state) && isValid
                        isValid = checkField(Shift_endtime, shift_endtime_state) && isValid
                        isValid = checkField(Shift_date, shift_date_state) && isValid

                        // PATIENT MEDICALS
                        isValid = checkField(user_doctorname, user_doctorname_state) && isValid
                        isValid = checkField(user_diagnosis, user_diagnosis_state) && isValid
                        isValid = checkField(user_medicalhistory, user_medicalhistory_state) && isValid
                        isValid = checkField(user_allergies, user_allergies_state) && isValid
                        isValid = checkField(user_painassesment, user_painassesment_state) && isValid

                        // Patient Vitals
                        isValid = checkField(user_vitalstime, user_vitals_state) && isValid
                        isValid = checkField(user_bp, user_bp_state) && isValid
                        isValid = checkField(user_heartrate, user_heartrate_state) && isValid
                        isValid = checkField(user_temp, user_temp_state) && isValid
                        isValid = checkField(user_oxygen, user_oxygen_state) && isValid
                        isValid = checkField(user_respirate, user_respirate_state) && isValid
                        isValid = checkField(user_pre_operative_order, user_pre_operative_order_state) && isValid
                        isValid = checkField(user_post_operative_ordre, user_post_operative_ordre_state) && isValid
                        isValid = checkField(user_procedure_status, user_procedure_status_state) && isValid
                        isValid = checkField(user_stat_medication, user_stat_medication_state) && isValid
                        isValid = checkField(user_doc_verbal_order_name, user_doc_verbal_order_name_state) && isValid
                        isValid = checkField(user_nurse_remarks, user_nurse_remarks_state) && isValid

                        if(isValid) {
                            var VitalsList = mutableListOf<PatientVitals>()
                            val vital = PatientVitals(
                                vitalreport_time = user_vitalstime.value,
                                bloodpressure = user_bp.value,
                                heartreate = user_heartrate.value,
                                temp = user_temp.value,
                                oxygenlevel = user_oxygen.value,
                                respiratoryrate = user_respirate.value
                            )
                            VitalsList.add(vital)
                            val PatientReport = PatientShiftReport(
                                shiftid = "$user_patientid" + "${Random.nextInt()}",
                                shift_startime = Shift_startime.value,
                                shift_endtime = Shift_endtime.value,
                                shift_date = Shift_date.value,
                                pInfo = P_ReportInfo(
                                    patientname = user_patientname.value,
                                    patientid = user_patientid.value,
                                    Gender = user_gender.value,
                                    wardno = user_wardno.value,
                                    dob = user_dob.value
                                ),
                                patientMedicalInfo = P_ReportMedicalInfo(
                                    doctors = user_doctorname.value,
                                    diagnosis = user_diagnosis.value,
                                    medicalhistory = user_medicalhistory.value,
                                    allergies = user_allergies.value,
                                    painlevels = user_painassesment.value,
                                    vitals_timelist = VitalsList
                                ),
                                pre_operative_order = user_pre_operative_order.value,
                                post_operative_ordre = user_post_operative_ordre.value,
                                procedure_status = user_procedure_status.value,
                                stat_medication = user_stat_medication.value,
                                doc_verbal_order_name = user_doc_verbal_order_name.value,
                                nurse_remarks = user_nurse_remarks.value
                            )

                            val pdf_file  : File = CreatePDF(PatientReport , context)
                            val pdfuri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider",  pdf_file)
                            // Create email intent
                            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_SUBJECT, "Patient Shift Report")
                                putExtra(Intent.EXTRA_TEXT, "Attached patient shift report")
                                putExtra(Intent.EXTRA_STREAM, pdfuri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(emailIntent, "Send report"))

                        }

                    }
                    // Direct Handover without saving the report
                    Button(onClick = { ValidateForm() },
                        colors = ButtonColors( containerColor = HTextClr , contentColor = Color.White , disabledContentColor = Color.Black , disabledContainerColor = Color.White ),
                        modifier = Modifier.size(width = 150.dp , height = 50.dp)) {
                        Text("Handover report" , fontFamily = Headingfont , fontWeight = FontWeight.Bold , fontSize = 13.sp)
                    }
                }


            }
        }

        TimePickerDialog(ShowTimeDialog) { time ->
            when(timepicktype.value) {
                TimePickTypeState.EndTIme -> Shift_endtime.value = "${time.hour}:${time.minute}"
                TimePickTypeState.StartTime ->       Shift_startime.value = "${time.hour}:${time.minute}"
                TimePickTypeState.VitalsRecordTime -> { user_vitalstime.value = "${time.hour}:${time.minute}" }
                else -> Unit
            }

        }
    }

 }


fun CreatePDF(report : PatientShiftReport , context : Context) : File {

    // Create Pdf using the IText Library
    val outputFile = File(context.cacheDir, "patient_report.pdf") // Create empty pdf file in cache
    val pdfWrite = com.itextpdf.kernel.pdf.PdfWriter(outputFile)
    val pdfdoc = com.itextpdf.kernel.pdf.PdfDocument(pdfWrite)
    val document = Document(pdfdoc , com.itextpdf.kernel.geom.PageSize.A4)

    // Header Section with Enhanced Spacing
    val headerParagraph = Paragraph().setMarginBottom(10f).setFontSize(16f)
    headerParagraph
        .add(Text("Patient Name: ").setBold())
        .add(Text(" " + report.pInfo.patientname + "         ").setUnderline())
        .add(Text("Patient Room: ").setBold())
        .add(Text(" " + report.pInfo.wardno + "        ").setUnderline())
        .add(Text("\nShift Timings: ")).setBold()
        .add(Text("${report.shift_startime} to ${report.shift_endtime}    "))
        .add(Text("Date:")).setBold()
        .add(Text("${report.shift_date}    "))
        .add(Text("\nDOB: ").setBold())
        .add(Text(" " + report.pInfo.dob + " ").setUnderline())
        .add(Text("Sex: ").setBold())
        .add(Text(" " + report.pInfo.Gender + " ").setUnderline())
    document.add(headerParagraph)

    // Medical Info Section with Spacing
    val medicalInfoParagraph = Paragraph().setMarginBottom(10f).setFontSize(16f)
    medicalInfoParagraph
        .add(Text("\nDoctors: ").setBold())
        .add(Text(" " + report.patientMedicalInfo.doctors + " ").setUnderline())
        .add(Text("\n\nDiagnosis: ").setBold())
        .add(Text(" " + report.patientMedicalInfo.diagnosis + " ").setUnderline())
        .add(Text("\n\nAllergies: ").setBold())
        .add(Text(" " + report.patientMedicalInfo.allergies + " ").setUnderline())
        .add(Text("\n\nHistory: ").setBold())
        .add(Text(" " + report.patientMedicalInfo.medicalhistory + " ").setUnderline())
    document.add(medicalInfoParagraph)

    // Vitals Section with Spacing
    document.add(Paragraph("\nVITALS:").setBold())
    report.patientMedicalInfo.vitals_timelist.forEachIndexed { index, vital ->
        val vitalsParagraph = Paragraph().setMarginBottom(10f).setFontSize(16f)
        vitalsParagraph
            .add(Text("Report Time : ${vital.vitalreport_time}"))
            .add(Text("BP:").setBold())
            .add(Text(" " + vital.bloodpressure + " ").setUnderline())
            .add(Text("HR:").setBold())
            .add(Text(" " + vital.heartreate + " ").setUnderline())
            .add(Text("Temp:").setBold())
            .add(Text(" " + vital.temp + " ").setUnderline())
            .add(Text("O2 Sat: ").setBold())
            .add(Text(" " + vital.oxygenlevel + " ").setUnderline())
            .add(Text("RR: ").setBold())
            .add(Text(" " + vital.respiratoryrate + " ").setUnderline())
        document.add(vitalsParagraph)
    }

    // Additional Information with Spacing
    val additionalInfoParagraph = Paragraph().setMarginBottom(20f).setFontSize(16f)
    additionalInfoParagraph
        .add(Text("\nDoctor Round Order/Pre-Operative Order: ").setBold())
        .add(Text(" " + report.pre_operative_order + " ").setUnderline())
        .add(Text("\n\nPost-Operative Order: ").setBold())
        .add(Text(" " + report.post_operative_ordre + " ").setUnderline())
        .add(Text("\n\nProcedure Status: ").setBold())
        .add(Text(" " + report.procedure_status + " ").setUnderline())
        .add(Text("\n\nStat Medication: ").setBold())
        .add(Text(" " + report.stat_medication + " ").setUnderline())
        .add(Text("\n\nDuty Doctor Verbal Order: ").setBold())
        .add(Text(" " + report.doc_verbal_order_name + " ").setUnderline())
        .add(Text("\n\nOther Remarks: ").setBold())
        .add(Text(" " + report.nurse_remarks + " ").setUnderline())
    document.add(additionalInfoParagraph)

    // Create signature section with two columns
    val signatureTable = Table(2).useAllAvailableWidth()

    document.add(Paragraph(Text("/n/n"))) // to provide extra spacing
    // Left column - Reporting Nurse
    val reportingNurseCell = Cell()
        .setBorder(Border3D.NO_BORDER)
        .add(Paragraph("_________________________").setTextAlignment(TextAlignment.CENTER))
        .add(Paragraph("Reporting Nurse Signature").setTextAlignment(TextAlignment.CENTER))

    // Right column - Oncoming Nurse
    val oncomingNurseCell = Cell()
        .setBorder(Border3D.NO_BORDER)
        .add(Paragraph("_________________________").setTextAlignment(TextAlignment.CENTER))
        .add(Paragraph("Oncoming Nurse Signature").setTextAlignment(TextAlignment.CENTER))

    signatureTable.addCell(reportingNurseCell)
    signatureTable.addCell(oncomingNurseCell)

    document.add(signatureTable)

    document.close()
    return outputFile
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    showDialog: MutableState<Boolean>,
    onTimeSelected: (LocalTime) -> Unit
){
    if(showDialog.value) {
        Dialog(onDismissRequest ={  showDialog.value = false } ) {
            val today = LocalTime.now()
            Column(modifier = Modifier.fillMaxSize().background(Color.Transparent) ,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val timepickstate = TimePickerState(
                    initialHour = today.hour,
                    initialMinute = today.minute,
                    is24Hour = true
                )
                TimePicker(timepickstate ,
                    colors = TimePickerDefaults.colors(
                    timeSelectorUnselectedContentColor = Color.DarkGray,
                    timeSelectorSelectedContentColor = Color.White,
                    timeSelectorSelectedContainerColor = HTextClr,
                    timeSelectorUnselectedContainerColor = SecClr ,
                    periodSelectorBorderColor = Color.Transparent,

                    periodSelectorSelectedContentColor = Color.White,
                    periodSelectorUnselectedContentColor = Color.DarkGray,


                    periodSelectorSelectedContainerColor = HTextClr,
                    periodSelectorUnselectedContainerColor = SecClr,
                    clockDialColor = SecClr,
                    clockDialSelectedContentColor = HTextClr,
                    clockDialUnselectedContentColor = Color.DarkGray,
                    selectorColor = Color.White,
                )
                )
                Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = { showDialog.value = false } ,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SecClr,
                            contentColor = Color.DarkGray
                        ),
                        modifier = Modifier.padding(start = 50.dp).background(color = SecClr , shape = ButtonDefaults.shape )
                            .border(0.1.dp , color = Color.Black.copy(alpha = 0.2f), shape = ButtonDefaults.shape ),
                        shape = ButtonDefaults.shape  ){
                        Text("Cancel" , fontSize = 12.sp )
                    }
                    Button(onClick = {
                            onTimeSelected(LocalTime(hour = timepickstate.hour, minute = timepickstate.minute))
                            showDialog.value = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = HTextClr,
                            contentColor = Color.White),
                        modifier = Modifier.padding(end = 50.dp).background(color = HTextClr , shape = ButtonDefaults.shape )
                            .border(0.1.dp , color = Color.Black.copy(alpha = 0.2f), shape = ButtonDefaults.shape ),
                        shape = ButtonDefaults.shape
                    ) {
                        Text("Confirm" , fontSize = 12.sp)
                    }
                }
            }
        }
    }
}



@file:Suppress("NonAsciiCharacters", "MoveLambdaOutsideParentheses")

package com.stackmobike.beberagua_lembrete

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.stackmobike.beberagua_lembrete.databinding.ActivityMainBinding
import com.stackmobike.beberagua_lembrete.model.CalcularIngestãoDiaria
import java.text.DecimalFormat
import java.util.*

@Suppress("PrivatePropertyName", "FunctionName", "unused")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var edit_peso: EditText
    private lateinit var edit_idade: EditText
    private lateinit var bt_calcular: Button
    private lateinit var txt_redesultado_ml: TextView
    private lateinit var ic_redefinir_dados: ImageView
    private lateinit var bt_lembrete: Button
    private lateinit var bt_alarme: Button
    private lateinit var txt_hora: TextView
    private lateinit var txt_minuto: TextView

    private lateinit var calcularIngestãoDiaria: CalcularIngestãoDiaria
    private var resultadoML = 0.0

    //Criando variaveis para o timer e Calendario do botão lembrete

    lateinit var timePickerDialog: TimePickerDialog
    lateinit var calendario: Calendar
    var horaAtual = 0
    var minutosAtuais = 0

    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        // IniciarComoponentes()
        calcularIngestãoDiaria = CalcularIngestãoDiaria()


        binding.btCalcular.setOnClickListener {
            if (binding.editPeso.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_peso, Toast.LENGTH_SHORT).show()
            } else if (binding.editIdade.text.toString().isEmpty()) {
                Toast.makeText(this, R.string.toast_informe_idade, Toast.LENGTH_SHORT).show()

                //Convertendo String para Double e Int
            } else {
                val peso = binding.editPeso.text.toString().toDouble()
                val idade = binding.editIdade.text.toString().toInt()
                calcularIngestãoDiaria.CalcularTotalML(peso, idade)
                resultadoML = calcularIngestãoDiaria.ResultadoML()
                val formatar = DecimalFormat("#######.##")
                binding.txtResultadoMl.text = formatar.format(resultadoML) + "" + "ml"
            }

        }
        // Criando caixa de dialogo quando clicar no redefinir"
        binding.icRedefinir.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this)
            alertDialog.setTitle(R.string.dialog_titulo)
                .setMessage(R.string.dialog_desc)

                // Criando o Botão OK da caixa de dialogo Redefinit e apagando os dados
                .setPositiveButton("OK", { dialogInterface, i ->
                    binding.editPeso.setText("")
                   binding.editIdade.setText("")
                    binding.txtResultadoMl.text = ""
                })
            //Criando o botão de cancelar da caixa de dialogo Redfinir
            alertDialog.setNegativeButton("Cancelar", { dialogInterface, i -> })

            val dialog = alertDialog.create()
            dialog.show()

        }
        binding.btDefinirLembrete.setOnClickListener {

            // Incluindo a caixa de dialogo para a hora e os minutos atuais

            calendario = Calendar.getInstance()
            horaAtual = calendario.get(Calendar.HOUR_OF_DAY)
            minutosAtuais = calendario.get(Calendar.MINUTE)
            timePickerDialog =
                TimePickerDialog(this, { timePicker: TimePicker, hourOfDay: Int, minutes: Int ->
                    binding.txtHora.text = String.format("%02d", hourOfDay)
                    binding.txtMinutos.text = String.format("%02d", minutes)
                }, horaAtual, minutosAtuais, true)
            timePickerDialog.show()
        }
        // Definindo o Botão alarme
        binding.btAlarme.setOnClickListener {

            if (!binding.txtHora.text.toString().isEmpty() && !binding.txtMinutos.text.toString()
                    .isEmpty()
            ) {
                //Ação de alarme do celular
                val intent = Intent(AlarmClock.ACTION_SET_ALARM)
                intent.putExtra(AlarmClock.EXTRA_HOUR, binding.txtHora.text.toString().toInt())
                intent.putExtra(
                    AlarmClock.EXTRA_MINUTES,
                    binding.txtMinutos.text.toString().toInt()
                )
                intent.putExtra(AlarmClock.EXTRA_MESSAGE, getString(R.string.alarme_mensagem))
                startActivity(intent)

                // if (caso seja nulo)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }

            }

        }
    }
}




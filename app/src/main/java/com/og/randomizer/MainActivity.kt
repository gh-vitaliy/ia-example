package com.og.randomizer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

//константа для ключа сохранения состояния
private const val LAST_SELECTED_ITEM = "LAST_SELECTED_ITEM"
private val NUMBER_FRAGMENT = NumberFragment().javaClass.name
private val DICE_FRAGMENT = DiceFragment().javaClass.name

class MainActivity : AppCompatActivity() {

    private var numberFragment = NumberFragment()
    private var diceFragment = DiceFragment()

    //ранняя инициализация нижней навигации
    private lateinit var bottomNavigationMenu: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationMenu = findViewById(R.id.bottom_navigation_menu)

        //натсроим клики по элементам нижней навигации
        bottomNavigationMenu.setOnItemSelectedListener { item ->
            var fragment: Fragment? = null
            when (item.itemId) {
                R.id.number -> {
                    fragment =
                        savedInstanceState?.let {
                            supportFragmentManager.getFragment(it, NUMBER_FRAGMENT)
                        } ?: numberFragment
                }
                R.id.dice -> {
                    fragment =
                        savedInstanceState?.let {
                            supportFragmentManager.getFragment(it, DICE_FRAGMENT)
                        } ?: diceFragment
                }
            }
            replaceFragment(fragment!!)
            true
        }

        //восстановление состояния нижней навиагции
        //если не сохранено то по дефолту выбрать R.id.number
        bottomNavigationMenu.selectedItemId =
            savedInstanceState?.getInt(LAST_SELECTED_ITEM) ?: R.id.number
    }

    //сохрнаим состояние последнего нажатого элемента нижней навигации
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(LAST_SELECTED_ITEM, bottomNavigationMenu.selectedItemId)

        //сохраняем интсанцию конкретного фрагмента
        val fragment = supportFragmentManager.fragments.last()
        supportFragmentManager.putFragment(outState, fragment.javaClass.name, fragment)
        super.onSaveInstanceState(outState)
    }

    //функция замены фрагментов с помощью supportFragmentManager
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
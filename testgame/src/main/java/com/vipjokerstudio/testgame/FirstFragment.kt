package com.vipjokerstudio.testgame

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vipjokerstudio.cocoskotlin.core.nodes.CCSprite
import com.vipjokerstudio.cocoskotlin.core.nodes.Director
import com.vipjokerstudio.cocoskotlin.core.types.CGRect
import com.vipjokerstudio.cocoskotlin.core.types.Size
import com.vipjokerstudio.testgame.databinding.FragmentFirstBinding
import org.jbox2d.common.Vec2

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        val director = Director.getInstance()
        binding.textviewFirst.setRenderer(director)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val instance = Director.getInstance()
        val scene = TestScene()
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height: Int = displayMetrics.heightPixels
        val width: Int = displayMetrics.widthPixels
        val playerbitmap = BitmapFactory.decodeResource(resources,R.raw.car_red)
        val board = BitmapFactory.decodeResource(resources,R.raw.boar)
        val mainBackground = BitmapFactory.decodeResource(resources, R.raw.bg)

        val player = Player(playerbitmap)


        val bg = CCSprite.sprite(mainBackground, CGRect(Vec2(0f,0f), Size.make(width.toFloat(),height.toFloat())))
        val boardSprt = CCSprite.sprite(board)
        bg.addChild(boardSprt)

        boardSprt.addChild(player)
        boardSprt.setContentSize(Size.make(width.toFloat(),width.toFloat()))



        scene.addChild(bg)

//        scene.addChild(player)
        instance.pushScene(scene)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
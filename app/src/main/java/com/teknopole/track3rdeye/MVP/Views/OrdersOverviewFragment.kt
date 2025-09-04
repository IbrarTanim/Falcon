package com.teknopole.track3rdeye.MVP.Views


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.teknopole.track3rdeye.MVP.Contracts.OrdersOverviewFragmentContract
import com.teknopole.track3rdeye.ObjectModels.TaskObject
import com.teknopole.track3rdeye.PopupAndDialogs.MenuTaskFilter
import com.teknopole.track3rdeye.R
import com.teknopole.track3rdeye.Utils.CircularAnimationUtils
import kotlinx.android.synthetic.main.fragment_orders_overview.*


class OrdersOverviewFragment : Fragment(), OrdersOverviewFragmentContract.View {
    private var menuTaskFiler: MenuTaskFilter? = null
    var taskFilterBY: String = "Assigned"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_orders_overview, container, false)

        view.addOnLayoutChangeListener(onLayoutChangeListener)
        // Inflate the layout for this fragment
        return view
    }

    private val onLayoutChangeListener = View.OnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
        removeLayoutChangeListener()
        CircularAnimationUtils().RegisterCircularRevealEnterAnimation(v
                , HomeActivity.touchPosition.x
                , HomeActivity.touchPosition.y
                , v.width, v.height
                , Color.parseColor("#ff7472")
                , Color.WHITE)
    }

    private fun removeLayoutChangeListener() {
        view!!.removeOnLayoutChangeListener(onLayoutChangeListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMember()
        initEvent()
    }

    override fun onPause() {
        super.onPause()
        menuTaskFiler?.Close()
    }

    override fun onStart() {
        super.onStart()
//        LiveChange.ListFragment.registerLiveChangeListener(context!!, this)
    }

    override fun onStop() {
        super.onStop()
//        LiveChange.TaskListFragment.unregisterLiveChangeListener(context!!)
    }

    private fun initMember() {
        val tabPagerAdapter = TabPagerAdapter(fragmentManager!!)
        pager.adapter = tabPagerAdapter
    }

    private fun initEvent() {
        btnBack.setOnClickListener { activity?.onBackPressed() }
        btnMenu.setOnClickListener {
            menuTaskFiler = MenuTaskFilter(context!!, taskFilterBY, object : MenuTaskFilter.MenuSelectListener {
                override fun FilterBy(filterBy: String) {
                    taskFilterBY = filterBy
//                    presenter.RequestToLoadTaskList(taskFilterBY)
                }
            })
            menuTaskFiler?.Show(it)
        }
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        tabs.setupWithViewPager(pager)
        pager.setCurrentItem(0)
        // first time load
//        presenter.RequestToLoadTaskList(taskFilterBY)
    }


    fun RequestRefreshing() {
//        presenter.RequestRefreshTaskList(taskFilterBY)
    }

    // tabadapter
    inner class TabPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> {
                    return DealerListFragment()
                }
                1 -> {
                    return OrderListFragment()
                }
                else -> return null
            }
        }

        override fun getCount(): Int {
            return 2
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {
                0 -> return "Dealers"
                1 -> return "Orders"
                else -> return "Default"
            }
        }


    }

    //============ Invoked by view ==============


    //============ Event ============
    private lateinit var listener: ActionListener

    fun SetEventListener(listener: ActionListener) {
        this.listener = listener
    }

    interface ActionListener {
        fun OnTaskItemSelectedShowTaskDetailsFragment(task: TaskObject)
    }
}
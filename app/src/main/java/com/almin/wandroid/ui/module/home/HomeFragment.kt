package com.almin.wandroid.ui.module.home

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.os.bundleOf
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.almin.arch.viewmodel.Contract
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.wandroid.R
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.databinding.FragmentTabHomeBinding
import com.almin.wandroid.ui.AppContract
import com.almin.wandroid.ui.base.AbsTabFragment
import com.almin.wandroid.ui.compose.RefreshLazyColumn
import com.almin.wandroid.ui.navigator.AppNavigator
import com.almin.wandroid.ui.navigator.appNavigator
import com.almin.wandroid.ui.widget.StatusBarUtil.setAppbarTopPadding
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import org.koin.androidx.viewmodel.ext.android.viewModel


// border padding detail  : https://stackoverflow.com/questions/64206648/jetpack-compose-order-of-modifiers
// ConstraintLayout  :  https://www.jianshu.com/p/5c730f9e3b36
// compose demo doc : https://docs.compose.net.cn/design/animation/animatestate/
// paging3 loadmore : https://medium.com/simform-engineering/list-view-with-pagination-using-jetpack-compose-e131174eac8e

// 推荐 + 每日一问
class HomeFragment : AbsTabFragment<FragmentTabHomeBinding, PageState, Contract.PageEffect, HomeViewModel>(FragmentTabHomeBinding::inflate) {
    //    sharedViewModel
    override val viewModel: HomeViewModel by viewModel()

    override fun lazyLoadData() {
        super.lazyLoadData()
        viewModel.setEvent(HomeContract.PageEvent.Refresh)
    }

    var firstInit = true

    override fun initView(rootView: View) {
        setAppbarTopPadding(binding.appbarLayout.root)
        (activity as AppCompatActivity).setSupportActionBar(binding.appbarLayout.toolbar)
//        binding.appbarLayout.toolbar.setLogo(R.drawable.ic_collect_active)
        binding.appbarLayout.toolbar.title = "首页"

        binding.cvHome.setContent {
            MaterialTheme {
                val isRefreshing by viewModel.isRefreshing.collectAsState()
                val pager = viewModel.viewStates.articlePaging.collectAsLazyPagingItems()
                val banners = viewModel.viewStates.banners
                val topArticles = viewModel.viewStates.topArticles

                RefreshLazyColumn(firstInit, isRefreshing, pager = pager, refresh = {
                    viewModel.setEvent(HomeContract.PageEvent.Refresh)
                    pager.refresh()
                }) { coroutineScope, listState ->
                    item(key = "top_banner"){
                        Banner(banners){
                            appNavigator().display(R.id.navigation_web,
                                AppNavigator.NavigationType.Add, bundleOf("url" to it.url, "title" to it.title))
                        }
//                        coroutineScope.launch {
//                            listState.animateScrollToItem(0)
//                        }
                    }
                    if(!topArticles.isNullOrEmpty()){
                        itemsIndexed(topArticles, key = { _, item ->
                            item.id
                        }){ _, item ->
                            ArticleItemCard(item = item){
                                //click
                                appNavigator().display(R.id.navigation_web,
                                    AppNavigator.NavigationType.Add, bundleOf("url" to item.link, "title" to item.title))
                            }
                        }
                    }
                    items(pager, key = {
                        it.id
                    }){
                        it?.run {
                            ArticleItemCard(item = this){
                                //click
                                appNavigator().display(R.id.navigation_web,
                                    AppNavigator.NavigationType.Add, bundleOf("url" to it.link, "title" to it.title))
                            }
                        }
                    }
                    item(key = "bottom_spacer") {
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }

    override fun handleState(state: PageState) {
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.action_search) {

            true
        } else super.onOptionsItemSelected(item)
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Banner(banners: List<Banner>?, click: (Banner) -> Unit){
        Box {
            if(banners.isNullOrEmpty()){
//                Image(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp)
//                        .height(200.dp)
//                        .clip(RoundedCornerShape(8.dp)),
//                    painter = painterResource(R.drawable.ic_dashboard_black_24dp),
//                    contentDescription = "",
//                    contentScale = ContentScale.Crop
//                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)){

                }
            }else{
                val bannerState = rememberPagerState()
                //自动滚动
                LaunchedEffect(bannerState.currentPage) {
                    if (bannerState.pageCount > 0) {
                        delay(2000)
                        bannerState.animateScrollToPage((bannerState.currentPage + 1) % (bannerState.pageCount))
                    }
                }

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    count = banners.size,
                    state = bannerState) { page ->
                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .height(200.dp)
                            .clickable {
                                click(banners[page])
                            }
                            .clip(RoundedCornerShape(8.dp)),
                        painter = rememberCoilPainter(request = banners[page].imagePath),
                        contentDescription = "",
                        contentScale = ContentScale.Crop
                    )
                }
                HorizontalPagerIndicator(
                    pagerState = bannerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                )
            }
        }
    }

    @Composable
    fun ArticleItemCard(item: Article, onClick: ()-> Unit){
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .wrapContentHeight()
                .clickable { onClick() }, elevation = 5.dp) {
                ConstraintLayout {
                    val (author, title, releaseTime, tags, chapter, collect) = createRefs()
                    Text(text = item.author.ifEmpty { item.shareUser },
                        Modifier.constrainAs(author){
                            top.linkTo(parent.top, 8.dp)
                            start.linkTo(parent.start, 8.dp)
                        }, fontSize = 14.sp)

                    Text(text = item.title,
                        Modifier.constrainAs(title){
                            top.linkTo(author.bottom, 15.dp)
                            start.linkTo(author.start)
                            end.linkTo(releaseTime.end)
                            width = Dimension.fillToConstraints
                        }, fontWeight = FontWeight.Bold)

                    Text(text = item.niceDate,
                        Modifier.constrainAs(releaseTime){
                            top.linkTo(author.top)
                            end.linkTo(parent.end, 8.dp)
                            bottom.linkTo(author.bottom)
                        },
                        color = Color(0xFF888888),
                        fontSize = 12.sp)

                    Text(
                        text = "${item.superChapterName}·${item.chapterName}",
                        Modifier.constrainAs(chapter) {
                            top.linkTo(title.bottom, 15.dp)
                            start.linkTo(author.start)
                            bottom.linkTo(parent.bottom, 15.dp)
                        },
                        color = Color(0xFF018786),
                        fontSize = 10.sp,
                    )

//                    var isCollect by remember { mutableStateOf(item.collect) }
                    var change by remember { mutableStateOf(false) }
                    val buttonSize by animateDpAsState(
                        targetValue = if(change) 32.dp else 24.dp
                    )
                    if(buttonSize == 32.dp) {
                        change = false
                    }
                    IconButton(onClick = {
                        if(appViewModel.isLogined()){
//                        isCollect = !isCollect
                            item.collect = !item.collect
                            change = true
                        }else{
                            appViewModel.setEvent(AppContract.Event.Login)
                        }
                    }, modifier = Modifier.constrainAs(collect){
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }){
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(buttonSize),
                            tint = if(item.collect) Color.Red else Color.Gray
                        )}

                    LazyRow(modifier = Modifier.constrainAs(tags){
                        start.linkTo(author.end, 6.dp)
                        top.linkTo(author.top)
                        bottom.linkTo(author.bottom)
                    }) {
                        if(item.isTop){
                            item {
                                TagView("置顶", Color(0xFFFF0000))
                            }
                        }
                        if(item.isNew){
                            item {
                                TagView("新", Color(0xFFFF0000))
                            }
                        }
//                        if(!item.tags.isNullOrEmpty()) {
//                            items(item.tags){ tag ->
//                                TagView(tag.name, Color(0xFFDC514E))
//                            }
//                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun TagView(name: String, color: Color) {
        Box(
            modifier = Modifier
                .padding(2.dp, 0.dp, 2.dp, 0.dp)
                .border(1.dp, Color.Red)
                .padding(2.dp, 1.dp, 2.dp, 1.dp)
        ){
            Text(text = name,
                color = color,
                fontSize = 11.sp)
        }
    }

    override fun handleEffect(effect: Contract.PageEffect) {
    }

}
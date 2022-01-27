package com.almin.wandroid.ui.module.home

import android.view.View
import android.view.ViewGroup
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.almin.arch.viewmodel.Contract.PageState
import com.almin.wandroid.R
import com.almin.wandroid.data.model.Article
import com.almin.wandroid.data.model.Banner
import com.almin.wandroid.databinding.FragmentHomeBinding
import com.almin.wandroid.ui.base.AbsTabFragment
import com.almin.wandroid.ui.widget.StatusBarUtil.setAppbarTopPadding
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

// border padding detail  : https://stackoverflow.com/questions/64206648/jetpack-compose-order-of-modifiers
// ConstraintLayout  :  https://www.jianshu.com/p/5c730f9e3b36
// compose demo doc : https://docs.compose.net.cn/design/animation/animatestate/
// paging3 loadmore : https://medium.com/simform-engineering/list-view-with-pagination-using-jetpack-compose-e131174eac8e

class ReHomeFragment : AbsTabFragment<FragmentHomeBinding, PageState, HomeViewModel>(FragmentHomeBinding::inflate) {
    //    sharedViewModel
    override val viewModel: HomeViewModel by viewModel()

    override fun lazyLoadData() {
        viewModel.setEvent(HomeContract.PageEvent.Refresh)
    }

    override fun initView(rootView: View) {
        setAppbarTopPadding(rootView as ViewGroup?, R.id.appbar)
        binding.cvHome.setContent {
            MaterialTheme {
                Column {
                    RefreshLayout(viewModel = viewModel)
                }
            }
        }
    }

    @Composable
    fun RefreshLayout(viewModel: HomeViewModel){
        val isRefreshing by viewModel.isRefreshing.collectAsState()
        val pager = viewModel.viewStates.articlePaging.collectAsLazyPagingItems()

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing),
            onRefresh = {
                viewModel.setEvent(HomeContract.PageEvent.Refresh)
                pager.refresh()
            },
        ){
            Feed(viewModel.viewStates.topArticles, pager)
        }
    }

    @OptIn(ExperimentalPagerApi::class)
    @Composable
    fun Banner(banners: List<Banner>?){
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
            }else{
                val bannerState = rememberPagerState()
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
    fun Feed(topArticles: List<Article>?, pager: LazyPagingItems<Article>) {
        val listState = rememberLazyListState()
        val coroutineScope = rememberCoroutineScope()
        LazyColumn(state = listState) {
            item {
                Banner(viewModel.viewStates.banners)
                coroutineScope.launch {
                    listState.animateScrollToItem(0)
                }
            }
            if(!topArticles.isNullOrEmpty()){
                itemsIndexed(topArticles){ index, item ->
                    ArticleItemCard(item = item)
                }
            }
            items(pager){
                ArticleItemCard(item = it!!)
            }
            item {
                Spacer(modifier = Modifier.height(10.dp))
            }
            pager.loadState.run {
                when {
                    refresh is LoadState.Loading -> {
                    }
                    refresh is LoadState.Error -> {
                        val e = refresh as LoadState.Error
                        item{ LoadMoreError{ pager.retry() } }
                    }
                    append is LoadState.Loading -> {
                        item { LoadMoreLoading() }
                    }
                    append is LoadState.Error -> {
//                        val e = append as LoadState.Error
//                        e.error.localizedMessage
                        item{ LoadMoreError{ pager.retry() } }
                    }
                    append is LoadState.NotLoading -> {
                        if(append.endOfPaginationReached) item{ LoadMoreEnd() }
                    }
                }
            }
        }
    }

    @Composable
    fun ArticleItemCard(item: Article){
        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 0.dp, 8.dp, 0.dp)
                .wrapContentHeight(), elevation = 5.dp) {
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

                    Text(text = "${item.superChapterName}·${item.chapterName}",
                        Modifier.constrainAs(chapter){
                            top.linkTo(title.bottom, 15.dp)
                            start.linkTo(author.start)
                            bottom.linkTo(parent.bottom, 15.dp)
                        },
                        color = Color(0xFF018786),
                        fontSize = 10.sp,)

                    var isCollect by remember { mutableStateOf(item.collect) }
                    var change by remember { mutableStateOf(false) }
                    val buttonSize by animateDpAsState(
                        targetValue = if(change) 32.dp else 24.dp
                    )
                    if(buttonSize == 32.dp) {
                        change = false
                    }
                    IconButton(onClick = {
                        isCollect = !isCollect
                        item.collect = !item.collect
                        change = true
                    }, modifier = Modifier.constrainAs(collect){
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end)
                    }){
                        Icon(
                            Icons.Rounded.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(buttonSize),
                            tint = if(isCollect) Color.Red else Color.Gray
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
                        if(!item.tags.isNullOrEmpty()) {
                            items(item.tags){ tag ->
                                TagView(tag.name, Color(0xFFDC514E))
                            }
                        }
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

    @Composable
    fun LoadMoreError(retry: () -> Unit){
        Box(
            Modifier
                .fillMaxWidth()
                .padding(20.dp), contentAlignment = Alignment.Center){
            Text(modifier = Modifier.clickable { retry() }, text = "重试", fontSize = 16.sp)
        }
    }

    @Composable
    fun LoadMoreLoading(){
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFFF77B7A),
                modifier = Modifier
                    .padding(10.dp)
                    .height(50.dp)
            )
        }
    }

    @Composable
    fun LoadMoreEnd(){
        Box(
            Modifier
                .fillMaxWidth()
                .padding(20.dp), contentAlignment = Alignment.Center){
            Text(text = "没有更多了", fontSize = 16.sp)
        }
    }

    override fun handleState(state: PageState) {
    }

}
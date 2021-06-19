<template>
    <Page actionBarHidden="true">
        <GridLayout class="page" columns="1*" rows="60,1*,60">
            <FlexboxLayout class="top-bar" row="0" col="0" flexDirection="row"
                justifyContent="flex-end">
                <Image src="~/assets/icons/search.png" />
                <Image src="~/assets/icons/write.png" />
                <Image src="~/assets/icons/share.png" />
            </FlexboxLayout>
            <FlexboxLayout class="container" row="1" col="0"
                backgroundColor="#eee" flexDirection="column">
                <GridLayout class="card" columns="1*" rows="4*,1*,2*"
                    backgroundColor="#fff">
                    <FlexboxLayout class="poem" row="0" col="0"
                        flexDirection="row" justifyContent="flex-end">
                        <FlexboxLayout class="poem-line"
                            flexDirection="column" v-for="line in poem" v-bind:key="line">
                            <Label class="poem-word" v-for="word in line"  v-bind:key="word"
                                :text="word" />
                        </FlexboxLayout>
                    </FlexboxLayout>
                    <Label row="1" col="0"></Label>
                    <FlexboxLayout row="2" col="0" flexDirection="row">
                        <FlexboxLayout class="author" flexDirection="column"
                            justifyContent="flex-end">
                            <Label text="李"></Label>
                            <Label text="白"></Label>
                            <Label class="logo" text="唐"
                                backgroundColor="#4471b6"></Label>
                        </FlexboxLayout>
                    </FlexboxLayout>
                </GridLayout>
            </FlexboxLayout>
            <FlexboxLayout class="bottom-bar" row="2" col="0"
                flexDirection="row" justifyContent="space-around">
                <FlexboxLayout class="item" flexDirection="column">
                    <Image src="~/assets/icons/book_blue.png" />
                    <Label text="诗词"></Label>
                </FlexboxLayout>
                <FlexboxLayout class="item" flexDirection="column">
                    <Image class="find" src="~/assets/icons/find.png" />
                    <Label text="发现"></Label>
                </FlexboxLayout>
                <FlexboxLayout class="item" flexDirection="column">
                    <Image class="fun" src="~/assets/icons/fun.png" />
                    <Label text="诗娱"></Label>
                </FlexboxLayout>
                <FlexboxLayout class="item" flexDirection="column">
                    <Image src="~/assets/icons/user.png" />
                    <Label text="我的"></Label>
                </FlexboxLayout>
            </FlexboxLayout>
        </GridLayout>
    </Page>
</template>

<script>
    export default {
        data() {
            return {
                poem: [
                    "随风直到夜郎西",
                    "我寄愁心与明月",
                    "闻道龙标过无锡",
                    "杨花落尽子规啼"
                ]
            };
        }
    };
</script>
<style scoped>
    .top-bar {
        border-bottom-width: 1;
        border-bottom-color: rgba(0, 0, 0, 0.1);
    }

    .card {
        margin: 50 40;
        box-shadow: 5 5 5 rgba(0, 0, 0, 0.15);
        border-radius: 10;
    }

    .bottom-bar {
        border-top-width: 1;
        border-top-color: rgba(0, 0, 0, 0.1);
    }
    .top-bar{
        padding-right: 10;
    }
    .top-bar Image {
        width: 27;
        margin: 0 10;
    }

    .poem {
        margin-top: 50;
        margin-right: 20;
        font-weight: 300;
        font-size: 20;
        color: #555;
        font-family: "STLITI","STLITI";
    }

    .poem-line {
        margin: 0 8;
    }

    .author {
        margin-left: 20;
        margin-bottom: 20;
        font-weight: 500;
        font-size: 15;
        font-family: "STLITI","STLITI";
    }

    .author Label {
        padding: 2 4;
    }

    .author .logo {
        color: white;
        border-radius: 3;
        margin-top: 15;
    }

    .bottom-bar .item {
        text-align: center;
        padding-top: 5;
        font-family: "STKAITI","STKAITI";
    }

    .bottom-bar .item Image {
        height: 30;
    }
    .bottom-bar .item .find{
        height: 25;
        margin-top:2;
        margin-bottom: 3;
    }
    .bottom-bar .item .fun{
        height: 25;
        margin-top:2;
        margin-bottom: 3;
    }

    .bottom-bar .item Label {
        font-size: 12;
    }
</style>
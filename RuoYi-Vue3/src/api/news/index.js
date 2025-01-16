import request from '@/utils/request'

//获取所有新闻信息
export function getAll() {
    return request({
        url: '/news/getAll',
        method: 'post',
    })
}

//插入新闻信息
export function insertNews(a,b,c,d){
    return request({
        url: '/news/insertNews',
        method: 'post',
        data:{
            title:a,
            content:b,
            push_name:c,
            time:d
        }
    })
}

//删除新闻
export function deleteNews(id){
    return request({
        url: '/news/deleteNews',
        method: 'post',
        data:{
            id:id
        }
    })
}

//根据id获取新闻信息
export function getNewsById(id){
    return request({
        url: '/news/getNewsById',
        method: 'post',
        data:{
            id:id
        }
    })
}

//修改新闻内容
export function updateNews(a,b,c,d,e,f){
    return request({
        url: '/news/updateNews',
        method: 'post',
        data:{
            id:a,
            title:b,
            content:c,
            push_name:d,
            time:e,
            category:f
        }
    })
}

// 获取分类信息
export function classify(t){
    return request({
        url: '/news/classify',
        method: 'post',
        data:{
            text:t
        }
    })
}

// 根据分词
export function tokenize(t){
    return request({
        url: '/news/tokenize',
        method: 'post',
        data:{
            text:t
        }
    })
}

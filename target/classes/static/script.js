document.addEventListener('DOMContentLoaded', function () {
    fetchAnnouncements(1, 5);  // 初始化时获取第一页的公告数据
    fetchHouses();  // 获取房屋数据
});

// 获取公告列表并显示
function fetchAnnouncements(currentPage, pageSize) {
    fetch(`http://localhost:8080/announcement/page?currentPage=${currentPage}&pageSize=${pageSize}`, {
        headers: {
            'Accept': 'application/json; charset=UTF-8'  // 指定接收UTF-8编码的JSON数据
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('公告数据:', data);
            if (data.code !== 1) {
                alert('获取公告失败');
                return;
            }

            const tableBody = document.getElementById('announcement-table').getElementsByTagName('tbody')[0];
            tableBody.innerHTML = '';  // 清空当前表格内容

            if (data.data && Array.isArray(data.data.announcements) && data.data.announcements.length > 0) {
                data.data.announcements.forEach(announcement => {
                    const row = tableBody.insertRow();
                    row.innerHTML = `
                        <td>${announcement.id}</td>
                        <td>${announcement.title}</td>
                        <td>${announcement.content}</td>
                        <td>${formatDate(announcement.createdAt)}</td>
                        <td><button class="btn-danger" onclick="deleteAnnouncement(${announcement.id})">删除</button></td>
                    `;
                });
                createPagination(data.data.totalCount, currentPage, pageSize);
            } else {
                tableBody.innerHTML = '<tr><td colspan="5">没有找到公告信息。</td></tr>';
            }
        })
        .catch(error => console.error('请求公告失败:', error));
}

// 格式化日期
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString();  // 返回日期格式，如：2024/11/30
}

// 动态生成分页按钮
function createPagination(totalCount, currentPage, pageSize) {
    const totalPages = Math.ceil(totalCount / pageSize);
    const paginationContainer = document.getElementById('announcement-pagination');
    paginationContainer.innerHTML = '';

    const prevButton = document.createElement('button');
    prevButton.textContent = '上一页';
    prevButton.disabled = currentPage === 1;
    prevButton.onclick = function () {
        fetchAnnouncements(currentPage - 1, pageSize);
    };
    paginationContainer.appendChild(prevButton);

    for (let i = 1; i <= totalPages; i++) {
        const button = document.createElement('button');
        button.textContent = i;
        button.onclick = function () {
            fetchAnnouncements(i, pageSize);
        };
        if (i === currentPage) {
            button.classList.add('active');
        }
        paginationContainer.appendChild(button);
    }

    const nextButton = document.createElement('button');
    nextButton.textContent = '下一页';
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = function () {
        fetchAnnouncements(currentPage + 1, pageSize);
    };
    paginationContainer.appendChild(nextButton);
}

// 请求后端 API 获取房屋数据
function fetchHouses() {
    fetch('http://localhost:8080/house/page?currentPage=1&pageSize=5', {
        headers: {
            'Accept': 'application/json; charset=UTF-8'  // 指定接收UTF-8编码的JSON数据
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('房屋数据:', data);
            const houseListElement = document.getElementById('houseList');
            houseListElement.innerHTML = '';

            if (data.houses && data.houses.length > 0) {
                data.houses.forEach(house => {
                    const houseCard = document.createElement('div');
                    houseCard.classList.add('house-card');
                    houseCard.innerHTML = `
                        <h3>房屋编号: ${house.houseNumber}</h3>
                        <p>面积: ${house.area} 平方米</p>
                        <p>售价: ?${house.price}</p>
                        <p>状态: ${house.status === 'available' ? '可售' : '已售'}</p>
                        <button class="btn" onclick="deleteHouse(${house.id})">删除</button>
                    `;
                    houseListElement.appendChild(houseCard);
                });
            } else {
                houseListElement.innerHTML = '<p>没有找到房屋信息。</p>';
            }
        })
        .catch(error => {
            console.error('获取房屋数据失败:', error);
        });
}

document.addEventListener('DOMContentLoaded', function () {
    fetchAnnouncements(1, 5);  // ��ʼ��ʱ��ȡ��һҳ�Ĺ�������
    fetchHouses();  // ��ȡ��������
});

// ��ȡ�����б���ʾ
function fetchAnnouncements(currentPage, pageSize) {
    fetch(`http://localhost:8080/announcement/page?currentPage=${currentPage}&pageSize=${pageSize}`, {
        headers: {
            'Accept': 'application/json; charset=UTF-8'  // ָ������UTF-8�����JSON����
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('��������:', data);
            if (data.code !== 1) {
                alert('��ȡ����ʧ��');
                return;
            }

            const tableBody = document.getElementById('announcement-table').getElementsByTagName('tbody')[0];
            tableBody.innerHTML = '';  // ��յ�ǰ�������

            if (data.data && Array.isArray(data.data.announcements) && data.data.announcements.length > 0) {
                data.data.announcements.forEach(announcement => {
                    const row = tableBody.insertRow();
                    row.innerHTML = `
                        <td>${announcement.id}</td>
                        <td>${announcement.title}</td>
                        <td>${announcement.content}</td>
                        <td>${formatDate(announcement.createdAt)}</td>
                        <td><button class="btn-danger" onclick="deleteAnnouncement(${announcement.id})">ɾ��</button></td>
                    `;
                });
                createPagination(data.data.totalCount, currentPage, pageSize);
            } else {
                tableBody.innerHTML = '<tr><td colspan="5">û���ҵ�������Ϣ��</td></tr>';
            }
        })
        .catch(error => console.error('���󹫸�ʧ��:', error));
}

// ��ʽ������
function formatDate(dateString) {
    const date = new Date(dateString);
    return date.toLocaleDateString();  // �������ڸ�ʽ���磺2024/11/30
}

// ��̬���ɷ�ҳ��ť
function createPagination(totalCount, currentPage, pageSize) {
    const totalPages = Math.ceil(totalCount / pageSize);
    const paginationContainer = document.getElementById('announcement-pagination');
    paginationContainer.innerHTML = '';

    const prevButton = document.createElement('button');
    prevButton.textContent = '��һҳ';
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
    nextButton.textContent = '��һҳ';
    nextButton.disabled = currentPage === totalPages;
    nextButton.onclick = function () {
        fetchAnnouncements(currentPage + 1, pageSize);
    };
    paginationContainer.appendChild(nextButton);
}

// ������ API ��ȡ��������
function fetchHouses() {
    fetch('http://localhost:8080/house/page?currentPage=1&pageSize=5', {
        headers: {
            'Accept': 'application/json; charset=UTF-8'  // ָ������UTF-8�����JSON����
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log('��������:', data);
            const houseListElement = document.getElementById('houseList');
            houseListElement.innerHTML = '';

            if (data.houses && data.houses.length > 0) {
                data.houses.forEach(house => {
                    const houseCard = document.createElement('div');
                    houseCard.classList.add('house-card');
                    houseCard.innerHTML = `
                        <h3>���ݱ��: ${house.houseNumber}</h3>
                        <p>���: ${house.area} ƽ����</p>
                        <p>�ۼ�: ?${house.price}</p>
                        <p>״̬: ${house.status === 'available' ? '����' : '����'}</p>
                        <button class="btn" onclick="deleteHouse(${house.id})">ɾ��</button>
                    `;
                    houseListElement.appendChild(houseCard);
                });
            } else {
                houseListElement.innerHTML = '<p>û���ҵ�������Ϣ��</p>';
            }
        })
        .catch(error => {
            console.error('��ȡ��������ʧ��:', error);
        });
}

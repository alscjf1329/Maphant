$(document).ready(function() {
    // 함수 정의: 팝업 보여주기
    function showPopup(data, title) {
        const popup = window.open('', 'Popup', 'width=800,height=600');
        const popupContent = `
      <p>신고 내용: ${data[title + "ReportName"]}</p>
      <p>신고 시간: ${data[title + "ReportedAt"]}</p>
      <p>신고자 ID: ${data.reporterUserId}</p>
      <p>신고자 이메일: ${data.reporterUserEmail}</p>
    `;
        popup.document.title = title;
        popup.document.write(popupContent);
        popup.document.close();
    }

    // 이벤트 핸들러: 게시글 제재 버튼 클릭
    $('.sanction-button-board').click(function() {
        const boardId = $(this).data("board-id");
        if (confirm('이 게시글을 정지시키겠습니까?')) {
            $.post({
                url: '/admin/sanction/board',
                data: `boardId=${boardId}`,
                success: function(response) {
                    // 서버로부터 받은 응답을 처리합니다.
                    console.log(response);
                    // 예시: 페이지를 새로고침하여 변경사항을 반영합니다.
                    $("#content").load("/admin/board");
                },
                error: function(error) {
                    console.error('요청 중 에러 발생:', error);
                }
            });
        }
    });
    // 이벤트 핸들러: 게시글 정보 버튼 클릭
    $(".info-button-board").click(function() {
        const boardId = $(this).data("board-id");
        $.get({
            url: '/admin/reportInfo/board',
            data: { boardId: boardId },
            success: function(response) {
                const parser = JSON.parse(JSON.stringify(response));
                if (parser.success) {
                    showPopup(parser.data[0],'board');
                }
            },
            error: function(error) {
                console.error('요청 중 에러 발생:', error);
            }
        });
    });
    // 이벤트 핸들러: 댓글 제재 버튼 클릭
    $(".sanction-button-comment").click(function() {
        const commentId = $(this).data("comment-id");
        if (confirm('이 댓글을 정지시키겠습니까?')) {
            $.post({
                url: '/admin/sanction/comment',
                data: { commentId: commentId },
                success: function(response) {
                    // 서버로부터 받은 응답을 처리합니다.
                    console.log(response);
                    // 예시: 페이지를 새로고침하여 변경사항을 반영합니다.
                    $("#content").load("/admin/comment");
                },
                error: function(error) {
                    console.error('요청 중 에러 발생:', error);
                }
            });
        }
    });
    // 이벤트 핸들러: 댓글 정보 버튼 클릭
    $(".info-button-comment").click(function() {
        const commentId = $(this).data("comment-id");
        $.get({
            url: '/admin/reportInfo/comment',
            data: { commentId: commentId },
            success: function(response) {
                const parser = JSON.parse(JSON.stringify(response));
                if (parser.success) {
                    showPopup(parser.data[0],'comment');
                }
            },
            error: function(error) {
                console.error('요청 중 에러 발생:', error);
            }
        });
    });

    // 이벤트 핸들러: 유저 정지 버튼 클릭
    $(".user-sanction-button").click(function() {
        event.preventDefault();

        const days = parseInt($("#sanctionDays").val()) || 0;
        const hours = parseInt($("#sanctionHours").val()) || 0;
        const minutes = parseInt($("#sanctionMinutes").val()) || 0;

        const offset = 1000 * 60 * 60 * 9
        const totalMilliseconds = (days * 24 * 60 * 60 * 1000) + (hours * 60 * 60 * 1000) + (minutes * 60 * 1000);
        const deadlineAt = new Date().getTime() + totalMilliseconds + offset;

        const userId = $("#userId").val();
        const sanctionReason = $("#sanctionReason").val();

        if (userId && sanctionReason) {
            const data = {
                userId: parseInt(userId),
                deadlineAt: new Date(deadlineAt).toISOString(),
                sanctionReason: sanctionReason
            };

            $.post({
                url: '/admin/sanction/user',
                data: JSON.stringify(data),
                contentType: "application/json",
                success: function(response) {
                    if (response === false) {
                        alert('해당 유저는 이미 제재되었습니다.');
                    } else {
                        alert('유저가 정지되었습니다.');
                    }
                    $("#content").load("/admin/user");
                },
                error: function(error) {
                    console.error('User sanction request error:', error);
                }
            });
        } else {
            alert("유저 ID와 제재 이유를 입력해주세요.");
        }
    });
    // 이벤트 핸들러: 유저 정지 해제 버튼 클릭
    $(".unSanction-button").click(function() {
        const userId = $(this).data("user-id");
        if (confirm('이 유저의 정지를 해제하시겠습니까?')) {
            $.post({
                url: '/admin/unSanction/user',
                data: { userId: userId },
                success: function(response) {
                    // 서버로부터 받은 응답을 처리합니다.
                    console.log(response);
                    // 예시: 페이지를 새로고침하여 변경사항을 반영합니다.
                    $("#content").load("/admin/user");
                },
                error: function(error) {
                    console.error('요청 중 에러 발생:', error);
                }
            });
        }
    });
    $(".info-button-sanction").click(function() {
        const userId = $(this).data("user-id");
        $.get({
            url: '/admin/reportInfo/user',
            data: { userId: userId },
            success: function(response) {
                const parser = JSON.parse(JSON.stringify(response));
                if (parser.success) {
                    const data = parser.data[0];
                    const popup = window.open('', 'Popup', 'width=800,height=600');
                    const popupContent = `
            <p>정지 이유: ${data.sanctionReason}</p>
            <p>정지 종료 시간: ${data.deadlineAt}</p>
          `;
                    popup.document.title = '정지 정보';
                    popup.document.write(popupContent);
                    popup.document.close();
                }
            },
            error: function(error) {
                console.error('요청 중 에러 발생:', error);
            }
        })
    })
    $(".sort-button").click(function() {
        const sortType = $(this).data("sort-type");
        const section = $(this).data("section");
        let contentUrl = `/admin/${section}`;

        if (sortType !== "time") {
            contentUrl += `?sortType=${sortType}`;
        }
        setActiveMenu(this, contentUrl);
    });
});
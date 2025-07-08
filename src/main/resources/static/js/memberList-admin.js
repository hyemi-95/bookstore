// CSRF (form, meta 병행)
function getCsrfToken() {
    return $("input[name='_csrf']").val() || $("meta[name='_csrf']").attr("content") || "";
}

function getCsrfHeader() {
    return $("meta[name='_csrf_header']").attr("content") || "X-CSRF-TOKEN";
}

// 계정정지 버튼 클릭 → 모달 열기
$(document).on('click', '.suspend-btn:not(:disabled)', function () {
    const memberId = $(this).closest('tr').data('member-id');
    $('#suspend-member-id').val(memberId);
    $('#suspend-reason').val('');
    $('#suspend-popup').show();
});

// 계정정지 모달 닫기
function hideSuspendPopup() {
    $('#suspend-popup').hide();
}

// 상태 클릭 시 팝업 (무조건 팝업)
$(document).on('click', '.status-cell.clickable', function () {
    const reason = $(this).data('reason');
    $('#reason-text').text(reason ? reason : '정지 사유 없음');
    $('#reason-popup').show();
});

function hideReasonPopup() {
    $('#reason-popup').hide();
}

// 비동기 계정정지
$('#suspend-form').on('submit', function (e) {
    e.preventDefault();
    const memberId = $('#suspend-member-id').val();
    const reason = $('#suspend-reason').val().trim();
    if (!reason) {
        alert('정지 사유를 입력하세요.');
        return;
    }

    const csrf = getCsrfToken();
    const csrfHeader = getCsrfHeader();
    $.ajax({
        url: '/admin/members/' + memberId + '/suspend',
        type: 'POST',
        contentType: 'application/json',
        headers: {[csrfHeader]: csrf},
        data: JSON.stringify({reason}),
        success: function () {
            const row = $("tr[data-member-id='" + memberId + "']");
            row.addClass('suspended-row');
            row.find('.status-cell')
                .text('SUSPENDED')
                .css('color', 'crimson')
                .data('reason', reason)
                .addClass('clickable');<!--사유클릭-->
            //계정정지 버튼 제거
            row.find('.suspend-btn').remove();
            row.find('.restore-btn').remove();

            row.find('.manage-cell').html(
                ' <button type="button" class="restore-btn">계정해제</button>'
            );
            hideSuspendPopup();
            alert('계정이 정지되었습니다.');
        },
        error: function () {
            alert('에러! 관리자에게 문의');
        }
    });
});

// 계정해제 버튼 클릭 시
$(document).on('click', '.restore-btn', function () {
    const memberId = $(this).closest('tr').data('member-id');
    if (!confirm('이 계정의 정지를 해제하시겠습니까?')) return;
    const csrf = getCsrfToken();
    const csrfHeader = getCsrfHeader();
    $.ajax({
        url: '/admin/members/' + memberId + '/restore',
        type: 'POST',
        contentType: 'application/json',
        headers: {[csrfHeader]: csrf},
        success: function () {
            const row = $("tr[data-member-id='" + memberId + "']");
            row.removeClass('suspended-row');
            row.find('.status-cell')
                .text('NOMAL')
                .css('color', '') // 기본색
                .removeClass('clickable');
            // row.find('.suspend-btn').prop('disabled', false);
            row.find('.restore-btn').remove(); // 해제버튼 사라짐
            row.find('.suspend-btn').remove(); // 정지버튼 사라짐
            //계정정지 버튼 다시 추가
            row.find('.manage-cell').html(
                ' <button type="button" class="suspend-btn">계정정지</button>'
            );

            alert('계정 정지가 해제되었습니다.');
        },
        error: function () {
            alert('에러! 관리자에게 문의');
        }
    });
});

//상태이력버튼 클릭 시
$(document).on('click', '.history-btn', function () {
    var memberId = $(this).data('member-id');
    $.get('/admin/members/' + memberId + '/history', function (list) {
        var rows = '';
        if (list.length === 0) {
            rows = '<tr><td colspan="3">이력 없음</td></tr>';
        } else {
            list.forEach(function (item) {
                rows += '<tr>'
                    + '<td>' + item.createdDate + '</td>'
                    + '<td>' + item.historyType + '</td>'
                    + '<td>' + (item.reason || '-') + '</td>'
                    + '</tr>';
            });
        }
        $('#history-table tbody').html(rows);
        $('#history-modal').show();
    });
});

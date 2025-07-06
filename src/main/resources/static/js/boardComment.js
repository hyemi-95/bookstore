<!-- ===== 댓글 JS ===== -->

const boardId = typeof BOARD_ID !== 'undefined' ? BOARD_ID : 0;
const csrfToken = typeof CSRF_TOKEN !== 'undefined' ? CSRF_TOKEN : '';
const csrfHeader = typeof CSRF_HEADER !== 'undefined' ? CSRF_HEADER : '';
const commentAlert = document.getElementById('commentAlert');
let editingCommentId = null;

// 댓글 목록 불러오기
function loadComments() {
    fetch(`/api/comments/${boardId}`)
        .then(res => {
            if (!res.ok) throw new Error("댓글 불러오기 실패");
            return res.json();
        })
        .then(data => {
            renderCommentList(data);
            editingCommentId = null;
        })
        .catch(err => {
            showAlert(err.message, "error");
        });
}

// 댓글 목록 그리기
function renderCommentList(comments) {
    const listDiv = document.getElementById('commentList');
    if (!comments || comments.length === 0) {
        listDiv.innerHTML = '<p style="color: #888;">댓글이 없습니다.</p>';
        return;
    }
    let html = '<ul style="list-style:none; padding-left:0;">';
    comments.forEach(comment => {
        html += `
            <li style="margin-bottom:10px;">
                <strong>${comment.writerNickName || '익명'}</strong>
                <span style="color:#aaa;font-size:0.95em;"> (${comment.createdDate ? comment.createdDate.substring(0, 16).replace('T', ' ') : ''})</span><br>
                <span style="white-space:pre-line;">
                  ${comment.visible ? comment.content : '<span style="color:#aaa;">관리자에 의해 숨겨진 댓글입니다.</span>'}
                </span>
                <div style="margin-top:3px;">
                    ${comment.editable ? `<button onclick="editComment(${comment.id})">수정</button>` : ''}
                    ${comment.deletable ? `<button onclick="deleteComment(${comment.id})">삭제</button>` : ''}
                    ${comment.blindable ? `<button onclick="blindComment(${comment.id})">블라인드</button>` : ''}
                    ${comment.blindable && !comment.visible ? `<button onclick="unblindComment(${comment.id})">복구</button>` : ''}

                </div>
            </li>
            `;
    });
    html += '</ul>';
    listDiv.innerHTML = html;
}

// 페이지 진입 시 자동 호출
document.addEventListener('DOMContentLoaded', loadComments);

//댓글 등록 js이벤트 추가
document.getElementById('commentAddBtn').addEventListener('click', function () {
    const content = document.getElementById('commentContent').value.trim();

    if (!content) {
        showAlert("댓글을 입력하세요.", "error");
        return;
    }
    // Ajax POST
    fetch('/api/comments', {
        method: 'POST', headers: {
            'Content-Type': 'application/json', [csrfHeader]: csrfToken
        }, body: JSON.stringify({boardId: boardId, content: content})
    })
        .then(res => {
            if (!res.ok) throw new Error("댓글 등록 실패");
            return res.json();
        })
        .then(data => {
            document.getElementById('commentContent').value = '';
            showAlert("등록 완료!", "success");
            loadComments(); // 댓글 목록 새로고침
        })
        .catch(err => {
            showAlert(err.message, "error");
        });
});

//댓글 삭제
function deleteComment(commentId) {
    if (!confirm('정말 삭제하시겠습니까?')) return;

    fetch(`/api/comments/${commentId}`, {
        method: 'DELETE', headers: {[csrfHeader]: csrfToken}
    })
        .then(res => {
            if (!res.ok) throw new Error('댓글 삭제 실패');
            showAlert("삭제 완료!", "success");
            loadComments(); // 댓글 새로고침
        })
        .catch(err => {
            showAlert(err.message, "error");
        });
}

//댓글 수정
function editComment(commentId) {
    // 이미 수정 중이면 취소 처리
    if (editingCommentId !== null && editingCommentId !== commentId) {
        alert("이미 다른 댓글을 수정 중입니다.")
        return;
    }
    //이미 같은 댓글을 수정 중이면 취소
    if (editingCommentId === commentId) {
        return;
    }
    const commentElem = document.querySelector(`#commentList li button[onclick*="editComment(${commentId})"]`).closest('li');
    const originalContent = commentElem.querySelector('span[style*="white-space"]').innerText;

    commentElem.querySelector('span[style*="white-space"]').innerHTML = `<textarea id="editContent" rows="2" style="width:90%;">${originalContent}</textarea>
         <button onclick="saveEditComment(${commentId})">저장</button>
         <button onclick="loadComments()">취소</button>`;
    editingCommentId = commentId;
}

//수정 - 저장
function saveEditComment(commentId) {
    const newContent = document.getElementById('editContent').value.trim();
    if (!newContent) {
        showAlert("내용을 입력하세요.", "error");
        return;
    }
    fetch(`/api/comments/${commentId}`, {
        method: 'PUT', headers: {
            'Content-Type': 'application/json', [csrfHeader]: csrfToken
        }, body: JSON.stringify({content: newContent})
    })
        .then(res => {
            if (!res.ok) throw new Error("수정 실패");
            showAlert("수정 완료!", "success");
            loadComments();
            editingCommentId = null;
        })
        .catch(err => {
            showAlert(err.message, "error");
        });
}

//블라인드 처리
function blindComment(commentId) {
    if (!confirm('정말 블라인드 처리하시겠습니까?')) return;
    fetch(`/api/comments/${commentId}/blind`, {
        method: 'PUT', headers: {[csrfHeader]: csrfToken}
    })
        .then(res => {
            if (!res.ok) throw new Error('블라인드 실패');
            loadComments();
        })
        .catch(err => {
            showAlert(err.message, "error");
        });
}

//블라인드 복구 처리
function unblindComment(commentId) {
    if (!confirm('정말 블라인드를 복구 하시겠습니까?')) return;
    fetch(`/api/comments/${commentId}/unblind`, {
        method: 'PUT', headers: {[csrfHeader]: csrfToken}
    })
        .then(res => {
            if (!res.ok) throw new Error('블라인드 복구 실패');
            loadComments();
        })
        .catch(err => {
            showAlert(err.message, "error");
        });
}

function showAlert(msg, type = "error") {
    commentAlert.innerText = msg;

    //기존 클래스 모두 제거
    commentAlert.classList.remove("alert-success", "alert-error");
    //type별 클래스 추가
    if (type === "success") {
        commentAlert.classList.add("alert-success");
    } else {
        commentAlert.classList.add("alert-error");
    }

    setTimeout(() => {
        if (commentAlert.innerText === msg) {
            commentAlert.innerText = "";
            commentAlert.classList.remove("alert-success", "alert-error");
        }
    }, 2000);
}

// 게시글 제출
export function handlePostSubmit(e) {
    e.preventDefault();

    const post = {
        title: document.getElementById("title").value,
        writer: document.getElementById("writer").value,
        board: document.getElementById("board").value,
        content: document.getElementById("content").value,
        hashtags: document.getElementById("hashtags").value.trim().split(" ").filter(tag => tag)
    };

    const formData = new FormData();
    const jsonBlob = new Blob([JSON.stringify(post)], { type: "application/json"});
    formData.append("post", jsonBlob);

    const imageFile = document.getElementById("image").files[0];
    if(imageFile) {
        formData.append("image", imageFile);
    }

    fetch("/apu/post", {
        method: "POST",
        body: formData
    })
        .then(res => res.json())
        .then(data => {
            alert("게시글이 작성되었습니다.")
            location.href = "/";
        })
        .catch(error => {
            console.error(error);
            alert(error.message || "게시글 등록 중 오류가 발생했습니다.");
        });
}
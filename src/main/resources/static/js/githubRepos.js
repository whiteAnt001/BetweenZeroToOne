function loadRepos() {
    fetch('/github/repos')
        .then(res => {
            if(!res.ok){
                alert("GitHub 인증이 필요합니다.");
                location.href="http://localhost:8077/oauth2/authorization/github";
                return Promise.reject("Redirecting to login");
            }
            return res.json();
        })
        .then(data => populateRepoSelect(data))
        .catch(err => {
            if(err !== "Redirecting to login") {
                alert(err);
            }
        });
}

function populateRepoSelect(repos) {
    const select = document.getElementById('repoSelect');
    select.innerHTML = '<option value="">저장소 선택</option>';
    repos.forEach(repo => {
        const opt = document.createElement('option');
        opt.value = repo.html_url;
        opt.textContent = `${repo.name} - ${repo.description || ''}`;
        opt.dataset.name = repo.name;
        opt.dataset.desc = repo.description || '';
        opt.dataset.url = repo.html_url;
        select.appendChild(opt);
    });
}

function insertRepoInfo() {
    const select = document.getElementById('repoSelect');
    const selectedOption = select.options[select.selectedIndex];

    if (!selectedOption.dataset.name) return;

    const name = selectedOption.dataset.name;
    const url = selectedOption.dataset.url;
    const desc = selectedOption.dataset.desc;

    const contentBox = document.getElementById('content');
    contentBox.value += `\n\n [${name}](${url})\n${desc}`;
}

// 전역에 등록
window.loadRepos = loadRepos;
window.insertRepoInfo = insertRepoInfo;

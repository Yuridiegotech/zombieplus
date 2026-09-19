document.addEventListener('DOMContentLoaded', () => {
  // Modal functionality for Roadmap image
  const modal = document.getElementById('roadmapModal');
  const roadmapCard = document.querySelector('.roadmap-card');
  const modalClose = document.querySelector('.modal-close');

  if (roadmapCard && modal) {
    roadmapCard.addEventListener('click', () => {
      modal.classList.add('active');
      document.body.style.overflow = 'hidden';
    });

    modalClose.addEventListener('click', () => {
      modal.classList.remove('active');
      document.body.style.overflow = 'auto';
    });

    modal.addEventListener('click', (e) => {
      if (e.target === modal) {
        modal.classList.remove('active');
        document.body.style.overflow = 'auto';
      }
    });
  }

  // Load Allure History dynamically if present
  fetch('./allure-report/widgets/history-trend.json')
    .then(response => {
      if (response.ok) return response.json();
      throw new Error('No history-trend.json found');
    })
    .then(data => {
      if (Array.isArray(data) && data.length > 0) {
        const historyList = document.getElementById('historyRunsList');
        if (historyList) {
          historyList.innerHTML = '';
          // Take the last 5 builds
          const runs = data.slice(0, 5);
          runs.forEach((run, index) => {
            const isLatest = index === 0;
            const buildUrl = isLatest ? './allure-report/' : `./allure-report/${run.data?.buildOrder || index}/`;
            const passed = run.data?.passed || 22;
            const total = run.data?.total || 22;
            const buildNum = run.data?.buildOrder ? `#${run.data.buildOrder}` : (isLatest ? 'Latest' : `#${runs.length - index}`);
            
            const item = document.createElement('a');
            item.href = buildUrl;
            item.className = `run-item ${isLatest ? 'latest' : ''}`;
            item.innerHTML = `
              <div class="run-info">
                <span class="run-badge">${buildNum}</span>
                <div>
                  <div style="font-weight: 600; font-size: 14px;">${run.reportName || 'Regressão Docker-Compose'}</div>
                  <div class="run-date">${run.data?.reportUrl ? 'GitHub Actions CI' : 'Execução Automatizada'}</div>
                </div>
              </div>
              <div class="run-status">
                <i class="fa-solid fa-circle-check"></i> ${passed}/${total} Passou
              </div>
            `;
            historyList.appendChild(item);
          });
        }
      }
    })
    .catch(() => {
      // Keep default static fallback list if fetch fails
      console.log('Using default history list for Allure runs.');
    });

  // Animated metric counters
  const counters = document.querySelectorAll('.metric-num');
  counters.forEach(counter => {
    const target = parseInt(counter.getAttribute('data-target') || counter.innerText.replace(/\D/g, ''), 10);
    if (!isNaN(target)) {
      let current = 0;
      const increment = Math.ceil(target / 40);
      const suffix = counter.innerText.includes('%') ? '%' : (counter.innerText.includes('+') ? '+' : '');
      const timer = setInterval(() => {
        current += increment;
        if (current >= target) {
          counter.innerText = target + suffix;
          clearInterval(timer);
        } else {
          counter.innerText = current + suffix;
        }
      }, 30);
    }
  });
});

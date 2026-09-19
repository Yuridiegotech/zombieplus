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

  // Load Allure Real Metrics & History dynamically (Last 3 - 5 runs)
  const historyUrls = [
    './allure-report/history-trend.json',
    './allure-report/latest/widgets/history-trend.json',
    './allure-report/40/widgets/history-trend.json'
  ];

  async function loadAllureData() {
    let historyData = null;
    for (const url of historyUrls) {
      try {
        const res = await fetch(url + '?t=' + Date.now());
        if (res.ok) {
          historyData = await res.json();
          if (Array.isArray(historyData) && historyData.length > 0) {
            break;
          }
        }
      } catch (e) {
        // try next
      }
    }

    if (historyData && Array.isArray(historyData) && historyData.length > 0) {
      const historyList = document.getElementById('historyRunsList');
      const latestRun = historyData[0];
      const latestBuildOrder = latestRun.buildOrder || latestRun.data?.buildOrder || 40;

      // Update Live stats with real data
      const passed = latestRun.data?.passed ?? 22;
      const total = latestRun.data?.total ?? 22;
      const failed = (latestRun.data?.failed || 0) + (latestRun.data?.broken || 0);
      const successRate = total > 0 ? Math.round((passed / total) * 100) : 100;

      const livePassedEl = document.querySelector('.report-stat-item .val.pass');
      if (livePassedEl) livePassedEl.textContent = `${passed} / ${total}`;

      const liveRateEl = document.querySelector('.report-stat-item .val.rate');
      if (liveRateEl) liveRateEl.textContent = `${successRate}%`;

      // Render Last 3-5 Runs in the History list
      if (historyList) {
        historyList.innerHTML = '';
        const runsToShow = historyData.slice(0, 5);
        runsToShow.forEach((run, index) => {
          const isLatest = index === 0;
          const buildNum = run.buildOrder || run.data?.buildOrder || (index === 0 ? latestBuildOrder : latestBuildOrder - index);
          const buildUrl = isLatest ? './allure-report/latest/' : `./allure-report/${buildNum}/`;
          const runPassed = run.data?.passed ?? 22;
          const runTotal = run.data?.total ?? 22;
          const runFailed = (run.data?.failed || 0) + (run.data?.broken || 0);

          const item = document.createElement('a');
          item.href = buildUrl;
          item.target = '_blank';
          item.className = `run-item ${isLatest ? 'latest' : ''}`;
          item.innerHTML = `
            <div class="run-info">
              <span class="run-badge">#${buildNum}</span>
              <div>
                <div style="font-weight: 600; font-size: 14px;">${isLatest ? 'Regressão Docker-Compose (Última)' : 'Execução Regressão CI'}</div>
                <div class="run-date">${isLatest ? 'Execução Mais Recente' : `Build #${buildNum}`}</div>
              </div>
            </div>
            <div class="run-status">
              <i class="fa-solid ${runFailed === 0 ? 'fa-circle-check' : 'fa-circle-xmark'}" style="color: ${runFailed === 0 ? 'var(--primary)' : 'var(--accent-red)'}"></i>
              ${runPassed}/${runTotal} Passou
            </div>
          `;
          historyList.appendChild(item);
        });
      }
    }
  }

  loadAllureData();

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

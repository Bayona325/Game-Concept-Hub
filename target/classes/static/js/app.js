// ===================== CONFIGURACIÓN =====================
const API_BASE_URL = '/api';
const API_GAMES = `${API_BASE_URL}/games`;
const API_AUTH = `${API_BASE_URL}/auth`;

// ===================== ESTADO GLOBAL =====================
const appState = {
    games: [],
    currentGame: null,
    currentSection: 'info',
    editingGameId: null,
    searchQuery: '',
    currentUser: null,
    isAuthenticated: false
};

// ===================== ELEMENTOS DEL DOM =====================
const elements = {
    // Vistas
    welcomeView: document.getElementById('welcomeView'),
    detailView: document.getElementById('detailView'),
    
    // Sidebar
    searchInput: document.getElementById('searchInput'),
    gamesList: document.getElementById('gamesList'),
    totalGames: document.getElementById('totalGames'),
    totalGenres: document.getElementById('totalGenres'),
    totalCategories: document.getElementById('totalCategories'),
    
    // Botones principales
    btnCreateGame: document.getElementById('btnCreateGame'),
    btnGoCreate: document.getElementById('btnGoCreate'),
    btnBack: document.getElementById('btnBack'),
    
    // Detail header
    gameTitle: document.getElementById('gameTitle'),
    gameGenreDisplay: document.getElementById('gameGenreDisplay'),
    tagsContainer: document.getElementById('tagsContainer'),
    
    // Section tabs
    sectionTabs: document.querySelectorAll('.section-tab'),
    
    // Content sections
    infoSection: document.getElementById('infoSection'),
    gameplaySection: document.getElementById('gameplaySection'),
    storySection: document.getElementById('storySection'),
    visualsSection: document.getElementById('visualsSection'),
    allSectionsSection: document.getElementById('allSectionsSection'),
    
    // Content displays
    gameDescriptionDisplay: document.getElementById('gameDescriptionDisplay'),
    categoriesDisplay: document.getElementById('categoriesDisplay'),
    gameplayContent: document.getElementById('gameplayContent'),
    storyContent: document.getElementById('storyContent'),
    visualsContent: document.getElementById('visualsContent'),
    allSectionsContainer: document.getElementById('allSectionsContainer'),
    
    // Action buttons
    btnEditInfo: document.getElementById('btnEditInfo'),
    btnDeleteGame: document.getElementById('btnDeleteGame'),
    btnAddGameplay: document.getElementById('btnAddGameplay'),
    btnAddStory: document.getElementById('btnAddStory'),
    btnAddVisuals: document.getElementById('btnAddVisuals'),
    btnAddNewSection: document.getElementById('btnAddNewSection'),
    
    // Modales
    gameModal: document.getElementById('gameModal'),
    sectionModal: document.getElementById('sectionModal'),
    btnCloseGameModal: document.getElementById('btnCloseGameModal'),
    btnCloseSectionModal: document.getElementById('btnCloseSectionModal'),
    btnCancelGame: document.getElementById('btnCancelGame'),
    btnCancelSection: document.getElementById('btnCancelSection'),
    gameForm: document.getElementById('gameForm'),
    sectionForm: document.getElementById('sectionForm'),
    
    // Form inputs
    gameName: document.getElementById('gameName'),
    gameGenre: document.getElementById('gameGenre'),
    gameDescription: document.getElementById('gameDescription'),
    gameCategories: document.getElementById('gameCategories'),
    gameTags: document.getElementById('gameTags'),
    sectionTitle: document.getElementById('sectionTitle'),
    sectionType: document.getElementById('sectionType'),
    sectionContent: document.getElementById('sectionContent'),
    
    // Modal titles
    modalTitle: document.getElementById('modalTitle'),
    sectionModalTitle: document.getElementById('sectionModalTitle'),
    
    // Toast
    toast: document.getElementById('toast'),
    
    // Auth elements
    btnLogout: document.getElementById('btnLogout'),
    userInfo: document.getElementById('userInfo'),
    username: document.getElementById('username')
};

// ===================== AUTENTICACIÓN =====================
async function checkAuthentication() {
    try {
        const response = await fetch(`${API_AUTH}/me`, {
            credentials: 'include'
        });
        
        if (response.ok) {
            const data = await response.json();
            appState.currentUser = data;
            appState.isAuthenticated = true;
            
            // Mostrar info de usuario
            if (elements.userInfo && elements.username) {
                elements.userInfo.style.display = 'inline';
                elements.username.textContent = data.username;
            }
            
            if (elements.btnLogout) {
                elements.btnLogout.style.display = 'inline-block';
            }
            
            return true;
        } else {
            redirectToLogin();
            return false;
        }
    } catch (error) {
        console.error('Error verificando autenticación:', error);
        redirectToLogin();
        return false;
    }
}

function redirectToLogin() {
    if (!window.location.pathname.includes('login')) {
        window.location.href = '/login.html';
    }
}

async function handleLogout() {
    try {
        await fetch(`${API_AUTH}/logout`, {
            method: 'POST',
            credentials: 'include'
        });
        
        appState.isAuthenticated = false;
        appState.currentUser = null;
        showToast('Sesión cerrada exitosamente', 'success');
        
        setTimeout(() => {
            redirectToLogin();
        }, 1000);
    } catch (error) {
        console.error('Error al cerrar sesión:', error);
        showToast('Error al cerrar sesión', 'error');
    }
}

// ===================== INICIALIZACIÓN =====================
document.addEventListener('DOMContentLoaded', async () => {
    console.log('Game Concept Hub cargado');
    
    // Verificar autenticación antes de cargar
    const isAuthenticated = await checkAuthentication();
    
    if (isAuthenticated) {
        loadGames();
        setupEventListeners();
        handleRouteChange();
        
        // Escuchar cambios de URL
        window.addEventListener('hashchange', handleRouteChange);
    }
});

// ===================== EVENT LISTENERS =====================
function setupEventListeners() {
    // Búsqueda
    elements.searchInput.addEventListener('input', handleSearch);
    
    // Logout
    if (elements.btnLogout) {
        elements.btnLogout.addEventListener('click', handleLogout);
    }
    
    // Crear juego
    elements.btnCreateGame.addEventListener('click', openCreateGameModal);
    elements.btnGoCreate.addEventListener('click', openCreateGameModal);
    
    // Regreso
    elements.btnBack.addEventListener('click', goToWelcome);
    
    // Tabs de secciones
    elements.sectionTabs.forEach(tab => {
        tab.addEventListener('click', () => switchSection(tab.dataset.section));
    });
    
    // Editar / Eliminar
    elements.btnEditInfo.addEventListener('click', editCurrentGame);
    elements.btnDeleteGame.addEventListener('click', deleteCurrentGame);
    
    // Añadir secciones
    elements.btnAddGameplay.addEventListener('click', () => openSectionModal('GAMEPLAY'));
    elements.btnAddStory.addEventListener('click', () => openSectionModal('STORY'));
    elements.btnAddVisuals.addEventListener('click', () => openSectionModal('VISUALS'));
    elements.btnAddNewSection.addEventListener('click', openSectionModal);
    
    // Formularios
    elements.gameForm.addEventListener('submit', handleGameFormSubmit);
    elements.sectionForm.addEventListener('submit', handleSectionFormSubmit);
    
    // Cerrar modales
    elements.btnCloseGameModal.addEventListener('click', closeGameModal);
    elements.btnCloseSectionModal.addEventListener('click', closeSectionModal);
    elements.btnCancelGame.addEventListener('click', closeGameModal);
    elements.btnCancelSection.addEventListener('click', closeSectionModal);
    
    // Cerrar modales con ESC
    document.addEventListener('keydown', (e) => {
        if (e.key === 'Escape') {
            closeGameModal();
            closeSectionModal();
        }
    });
}

// ===================== RUTEO (SPA) =====================
function handleRouteChange() {
    const hash = window.location.hash.slice(1);
    
    if (hash.startsWith('game/')) {
        const gameId = parseInt(hash.split('/')[1]);
        const game = appState.games.find(g => g.id === gameId);
        
        if (game) {
            showGameDetail(game);
        } else {
            goToWelcome();
        }
    } else {
        showWelcome();
    }
}

function showWelcome() {
    elements.welcomeView.classList.add('active');
    elements.detailView.classList.remove('active');
    appState.currentGame = null;
}

function goToWelcome() {
    window.location.hash = '';
    showWelcome();
}

// ===================== CARGAR JUEGOS =====================
async function loadGames() {
    try {
        const response = await fetch(`${API_GAMES}/search?query=`);
        
        if (!response.ok) {
            throw new Error('Error al cargar juegos');
        }
        
        const games = await response.json();
        appState.games = games || [];
        updateStats();
        renderGamesList(appState.games);
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al cargar los juegos', 'error');
    }
}

// ===================== BÚSQUEDA EN TIEMPO REAL =====================
function handleSearch(e) {
    const query = e.target.value.toLowerCase();
    appState.searchQuery = query;
    
    const filtered = appState.games.filter(game => 
        game.name.toLowerCase().includes(query)
    );
    
    renderGamesList(filtered);
}

// ===================== RENDERIZAR LISTA DE JUEGOS =====================
function renderGamesList(games) {
    if (!games || games.length === 0) {
        elements.gamesList.innerHTML = '<div class="empty-list"><p>📭 No hay juegos</p></div>';
        return;
    }
    
    elements.gamesList.innerHTML = games.map(game => `
        <div class="game-list-item ${appState.currentGame?.id === game.id ? 'active' : ''}" 
             data-game-id="${game.id}">
            <div class="game-list-item-title">${escapeHtml(game.name)}</div>
            <div class="game-list-item-genre">${escapeHtml(game.genre || 'Sin género')}</div>
        </div>
    `).join('');
    
    // Event listeners para items
    document.querySelectorAll('.game-list-item').forEach(item => {
        item.addEventListener('click', () => {
            const gameId = parseInt(item.dataset.gameId);
            const game = appState.games.find(g => g.id === gameId);
            if (game) {
                showGameDetail(game);
            }
        });
    });
}

// ===================== ACTUALIZAR ESTADÍSTICAS =====================
function updateStats() {
    elements.totalGames.textContent = appState.games.length;
    
    const genres = new Set(appState.games.map(g => g.genre).filter(Boolean));
    elements.totalGenres.textContent = genres.size;
    
    const categories = new Set();
    appState.games.forEach(game => {
        if (game.categories) {
            game.categories.forEach(cat => categories.add(cat.name));
        }
    });
    elements.totalCategories.textContent = categories.size;
}

// ===================== MOSTRAR DETALLE DEL JUEGO =====================
function showGameDetail(game) {
    appState.currentGame = game;
    
    // Actualizar URL con hash
    window.location.hash = `game/${game.id}`;
    
    // Mostrar vista de detalle
    elements.welcomeView.classList.remove('active');
    elements.detailView.classList.add('active');
    
    // Actualizar header
    elements.gameTitle.textContent = game.name;
    elements.gameGenreDisplay.textContent = game.genre || 'Sin género';
    
    // Renderizar tags
    renderTags(game);
    
    // Renderizar contenido
    renderGameInfo(game);
    renderGameSections(game);
    
    // Resetear sección activa
    switchSection('info');
    
    // Actualizar lista lateral
    renderGamesList(appState.searchQuery ? 
        appState.games.filter(g => g.name.toLowerCase().includes(appState.searchQuery)) :
        appState.games
    );
}

// ===================== RENDERIZAR TAGS =====================
function renderTags(game) {
    if (!game.tags || game.tags.length === 0) {
        elements.tagsContainer.innerHTML = '<p class="empty-text">Sin tags</p>';
        return;
    }
    
    elements.tagsContainer.innerHTML = game.tags
        .map(tag => `<span class="tag">#${escapeHtml(tag.name)}</span>`)
        .join('');
}

// ===================== RENDERIZAR INFO GENERAL =====================
function renderGameInfo(game) {
    elements.gameDescriptionDisplay.textContent = game.description || 'Sin descripción';
    
    if (!game.categories || game.categories.length === 0) {
        elements.categoriesDisplay.innerHTML = '<p class="empty-text">Sin categorías</p>';
    } else {
        elements.categoriesDisplay.innerHTML = game.categories
            .map(cat => `<span class="tag">${escapeHtml(cat.name)}</span>`)
            .join('');
    }
}

// ===================== RENDERIZAR SECCIONES =====================
function renderGameSections(game) {
    if (!game.sections || game.sections.length === 0) {
        elements.gameplayContent.innerHTML = '<p class="empty-text">No hay sección de Gameplay agregada aún.</p><button class="btn btn-primary" id="btnAddGameplay">+ Añadir Gameplay</button>';
        elements.storyContent.innerHTML = '<p class="empty-text">No hay sección de Historia agregada aún.</p><button class="btn btn-primary" id="btnAddStory">+ Añadir Historia</button>';
        elements.visualsContent.innerHTML = '<p class="empty-text">No hay sección de Visuales agregada aún.</p><button class="btn btn-primary" id="btnAddVisuals">+ Añadir Visuales</button>';
        elements.allSectionsContainer.innerHTML = '<p class="empty-text">Sin secciones agregadas aún.</p>';
        
        // Reactivar listeners de botones
        document.getElementById('btnAddGameplay')?.addEventListener('click', () => openSectionModal('GAMEPLAY'));
        document.getElementById('btnAddStory')?.addEventListener('click', () => openSectionModal('STORY'));
        document.getElementById('btnAddVisuals')?.addEventListener('click', () => openSectionModal('VISUALS'));
        return;
    }
    
    // Renderizar por tipo
    const gameplaySections = game.sections.filter(s => s.type === 'GAMEPLAY');
    const storySections = game.sections.filter(s => s.type === 'STORY');
    const visualsSections = game.sections.filter(s => s.type === 'VISUALS');
    
    elements.gameplayContent.innerHTML = gameplaySections.length > 0
        ? gameplaySections.map(s => renderSectionContent(s, game)).join('')
        : '<p class="empty-text">No hay sección de Gameplay.</p><button class="btn btn-primary" id="btnAddGameplay">+ Añadir Gameplay</button>';
    
    elements.storyContent.innerHTML = storySections.length > 0
        ? storySections.map(s => renderSectionContent(s, game)).join('')
        : '<p class="empty-text">No hay sección de Historia.</p><button class="btn btn-primary" id="btnAddStory">+ Añadir Historia</button>';
    
    elements.visualsContent.innerHTML = visualsSections.length > 0
        ? visualsSections.map(s => renderSectionContent(s, game)).join('')
        : '<p class="empty-text">No hay sección de Visuales.</p><button class="btn btn-primary" id="btnAddVisuals">+ Añadir Visuales</button>';
    
    // Renderizar todas las secciones
    if (game.sections.length > 0) {
        elements.allSectionsContainer.innerHTML = game.sections
            .map((s, idx) => `
                <div class="section-card">
                    <div class="section-card-title">${escapeHtml(s.title)}</div>
                    <div class="section-card-type">${s.type}</div>
                    <div class="section-card-content">${escapeHtml(s.content)}</div>
                </div>
            `)
            .join('');
    } else {
        elements.allSectionsContainer.innerHTML = '<p class="empty-text">Sin secciones agregadas aún.</p>';
    }
    
    // Reactivar listeners
    document.getElementById('btnAddGameplay')?.addEventListener('click', () => openSectionModal('GAMEPLAY'));
    document.getElementById('btnAddStory')?.addEventListener('click', () => openSectionModal('STORY'));
    document.getElementById('btnAddVisuals')?.addEventListener('click', () => openSectionModal('VISUALS'));
}

function renderSectionContent(section, game) {
    return `
        <div class="info-card">
            <h3>${escapeHtml(section.title)}</h3>
            <p>${escapeHtml(section.content)}</p>
        </div>
    `;
}

// ===================== NAVEGACIÓN POR SECCIONES (TABS) =====================
function switchSection(sectionName) {
    appState.currentSection = sectionName;
    
    // Actualizar tabs activo
    elements.sectionTabs.forEach(tab => {
        if (tab.dataset.section === sectionName) {
            tab.classList.add('active');
        } else {
            tab.classList.remove('active');
        }
    });
    
    // Actualizar contenido visible
    const sections = {
        'info': elements.infoSection,
        'gameplay': elements.gameplaySection,
        'story': elements.storySection,
        'visuals': elements.visualsSection,
        'sections': elements.allSectionsSection
    };
    
    Object.values(sections).forEach(section => section.classList.remove('active'));
    if (sections[sectionName]) {
        sections[sectionName].classList.add('active');
    }
}

// ===================== MODAL: CREAR/EDITAR JUEGO =====================
function openCreateGameModal() {
    appState.editingGameId = null;
    elements.modalTitle.textContent = 'Nuevo Juego';
    elements.gameForm.reset();
    elements.gameModal.classList.add('active');
}

function editCurrentGame() {
    if (!appState.currentGame) return;
    
    appState.editingGameId = appState.currentGame.id;
    elements.modalTitle.textContent = `Editar: ${appState.currentGame.name}`;
    
    elements.gameName.value = appState.currentGame.name;
    elements.gameGenre.value = appState.currentGame.genre || '';
    elements.gameDescription.value = appState.currentGame.description || '';
    elements.gameCategories.value = appState.currentGame.categories 
        ? appState.currentGame.categories.map(c => c.name).join(', ') 
        : '';
    elements.gameTags.value = appState.currentGame.tags 
        ? appState.currentGame.tags.map(t => t.name).join(', ') 
        : '';
    
    elements.gameModal.classList.add('active');
}

function closeGameModal() {
    elements.gameModal.classList.remove('active');
    elements.gameForm.reset();
    appState.editingGameId = null;
}

async function handleGameFormSubmit(e) {
    e.preventDefault();
    
    const gameData = {
        name: elements.gameName.value,
        genre: elements.gameGenre.value,
        description: elements.gameDescription.value,
        categories: parseArrayInput(elements.gameCategories.value),
        tags: parseArrayInput(elements.gameTags.value),
        sections: appState.currentGame?.sections || []
    };
    
    try {
        const endpoint = appState.editingGameId 
            ? `${API_GAMES}/${appState.editingGameId}`
            : API_GAMES;
        
        const method = appState.editingGameId ? 'PUT' : 'POST';
        
        const response = await fetch(endpoint, {
            method: method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gameData)
        });
        
        if (!response.ok) {
            throw new Error('Error al guardar');
        }
        
        closeGameModal();
        showToast(
            appState.editingGameId ? 'Juego actualizado correctamente' : 'Juego creado correctamente',
            'success'
        );
        
        await loadGames();
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al guardar el juego', 'error');
    }
}

async function deleteCurrentGame() {
    if (!appState.currentGame) return;
    
    if (!confirm(`¿Estás seguro de que deseas eliminar "${appState.currentGame.name}"?`)) {
        return;
    }
    
    try {
        const response = await fetch(`${API_GAMES}/${appState.currentGame.id}`, {
            method: 'DELETE'
        });
        
        if (!response.ok) {
            throw new Error('Error al eliminar');
        }
        
        showToast('Juego eliminado correctamente', 'success');
        goToWelcome();
        await loadGames();
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al eliminar el juego', 'error');
    }
}

// ===================== MODAL: AÑADIR SECCIÓN =====================
function openSectionModal(type = null) {
    if (!appState.currentGame) return;
    
    elements.sectionModalTitle.textContent = 'Añadir Sección';
    elements.sectionForm.reset();
    
    if (type) {
        elements.sectionType.value = type;
    }
    
    elements.sectionModal.classList.add('active');
}

function closeSectionModal() {
    elements.sectionModal.classList.remove('active');
    elements.sectionForm.reset();
}

async function handleSectionFormSubmit(e) {
    e.preventDefault();
    
    if (!appState.currentGame) return;
    
    const section = {
        title: elements.sectionTitle.value,
        type: elements.sectionType.value,
        content: elements.sectionContent.value
    };
    
    if (!appState.currentGame.sections) {
        appState.currentGame.sections = [];
    }
    
    appState.currentGame.sections.push(section);
    
    try {
        const response = await fetch(`${API_GAMES}/${appState.currentGame.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(appState.currentGame)
        });
        
        if (!response.ok) {
            throw new Error('Error al guardar la sección');
        }
        
        closeSectionModal();
        showToast('Sección añadida correctamente', 'success');
        
        // Recargar juego actual
        const updated = await response.json();
        showGameDetail(updated);
    } catch (error) {
        console.error('Error:', error);
        showToast('Error al añadir la sección', 'error');
    }
}

// ===================== UTILIDADES =====================
function showToast(message, type = 'success') {
    elements.toast.textContent = message;
    elements.toast.className = `toast show ${type}`;
    
    setTimeout(() => {
        elements.toast.classList.remove('show');
    }, 3000);
}

function parseArrayInput(input) {
    return input
        .split(',')
        .map(item => ({
            name: item.trim()
        }))
        .filter(item => item.name.length > 0);
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

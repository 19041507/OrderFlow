const state = {
    clientes: [],
    produtos: [],
    pedidos: [],
    itens: [],
    pagamentos: [],
    entregas: [],
    historico: [],
    paginas: {
        clientes: 1,
        produtos: 1,
        pedidos: 1,
        pagamentos: 1,
        entregas: 1,
        historico: 1
    },
    porPagina: 4
};

const endpoints = {
    clientes: '/clientes',
    produtos: '/produtos',
    pedidos: '/pedidos',
    itens: '/contem',
    pagamentos: '/pagamentos',
    entregas: '/entregas',
    historico: '/historico-status'
};

const money = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' });

function toast(message, type = 'success') {
    const el = document.getElementById('toast');
    el.textContent = message;
    el.className = `toast show ${type === 'error' ? 'error' : ''}`;
    setTimeout(() => {
        el.className = 'toast';
    }, 3200);
}

async function request(url, options = {}) {
    const response = await fetch(url, {
        headers: { 'Content-Type': 'application/json' },
        ...options
    });

    if (!response.ok) {
        let message = 'Erro ao executar operação.';
        try {
            const body = await response.json();
            message = body.erro || body.message || message;
        } catch (_) {}
        throw new Error(message);
    }

    if (response.status === 204) return null;
    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

function formToObject(form) {
    const data = Object.fromEntries(new FormData(form).entries());
    Object.keys(data).forEach(key => {
        if (data[key] === '') delete data[key];
        else if (key.startsWith('id') || key === 'numero' || key === 'quantidade' || key === 'estoque') data[key] = Number(data[key]);
        else if (key === 'preco' || key === 'valor') data[key] = Number(data[key]);
    });
    return data;
}

function clienteNome(id) {
    return state.clientes.find(cliente => cliente.idCliente === id)?.nome || `Cliente #${id}`;
}

function produtoNome(id) {
    return state.produtos.find(produto => produto.idProduto === id)?.nome || `Produto #${id}`;
}

function statusTexto(status) {
    return (status || 'PENDENTE').replaceAll('_', ' ');
}

function dataTexto(data) {
    return data ? new Date(data).toLocaleString('pt-BR') : 'Data não informada';
}

function foodEmoji(nome = '') {
    const lower = nome.toLowerCase();
    if (lower.includes('pizza')) return '🍕';
    if (lower.includes('hamb') || lower.includes('burger')) return '🍔';
    if (lower.includes('batata') || lower.includes('frita')) return '🍟';
    if (lower.includes('sushi')) return '🍣';
    if (lower.includes('bolo') || lower.includes('doce')) return '🍰';
    if (lower.includes('taco') || lower.includes('nacho')) return '🌮';
    if (lower.includes('salada')) return '🥗';
    if (lower.includes('suco')) return '🧃';
    if (lower.includes('refrigerante') || lower.includes('cola') || lower.includes('bebida')) return '🥤';
    return '🍽️';
}

function itensDaPagina(nome, lista) {
    const totalPaginas = Math.max(1, Math.ceil(lista.length / state.porPagina));
    if (state.paginas[nome] > totalPaginas) state.paginas[nome] = totalPaginas;
    if (state.paginas[nome] < 1) state.paginas[nome] = 1;
    const inicio = (state.paginas[nome] - 1) * state.porPagina;
    return lista.slice(inicio, inicio + state.porPagina);
}

function renderPaginacao(nome, lista) {
    const container = document.getElementById(`pagination-${nome}`);
    if (!container) return;

    const totalPaginas = Math.max(1, Math.ceil(lista.length / state.porPagina));
    if (lista.length <= state.porPagina) {
        container.innerHTML = lista.length ? `<span>Página 1 de 1</span>` : '';
        return;
    }

    const paginaAtual = state.paginas[nome];
    const botoes = Array.from({ length: totalPaginas }, (_, index) => {
        const pagina = index + 1;
        return `<button class="${pagina === paginaAtual ? 'active' : ''}" onclick="mudarPagina('${nome}', ${pagina})">${pagina}</button>`;
    }).join('');

    container.innerHTML = `
        <span>Página ${paginaAtual} de ${totalPaginas}</span>
        <button onclick="mudarPagina('${nome}', ${paginaAtual - 1})" ${paginaAtual === 1 ? 'disabled' : ''}>Anterior</button>
        ${botoes}
        <button onclick="mudarPagina('${nome}', ${paginaAtual + 1})" ${paginaAtual === totalPaginas ? 'disabled' : ''}>Próxima</button>
    `;
}

function mudarPagina(nome, pagina) {
    const lista = state[nome] || [];
    const totalPaginas = Math.max(1, Math.ceil(lista.length / state.porPagina));
    state.paginas[nome] = Math.min(Math.max(1, pagina), totalPaginas);
    renderTudo();
}
window.mudarPagina = mudarPagina;

function preencherSelect(selectId, items, valueKey, labelFn, emptyText) {
    const select = document.getElementById(selectId);
    if (!select) return;
    select.innerHTML = '';
    if (!items.length) {
        select.innerHTML = `<option value="">${emptyText}</option>`;
        return;
    }
    items.forEach(item => {
        const option = document.createElement('option');
        option.value = item[valueKey];
        option.textContent = labelFn(item);
        select.appendChild(option);
    });
}

function renderDashboard() {
    document.getElementById('total-clientes').textContent = state.clientes.length;
    document.getElementById('total-produtos').textContent = state.produtos.length;
    document.getElementById('total-pedidos').textContent = state.pedidos.length;

    const total = state.pedidos.reduce((sum, pedido) => sum + (pedido.valorTotal || 0), 0);
    document.getElementById('total-faturamento').textContent = money.format(total);

    const ultimoPedido = [...state.pedidos].sort((a, b) => (b.idPedido || 0) - (a.idPedido || 0))[0];
    if (ultimoPedido) {
        const item = state.itens.find(i => i.idPedido === ultimoPedido.idPedido);
        document.getElementById('preview-status').textContent = statusTexto(ultimoPedido.statusAtual);
        document.getElementById('preview-title').textContent = item ? `${foodEmoji(produtoNome(item.idProduto))} ${produtoNome(item.idProduto)}` : `🧾 Pedido #${ultimoPedido.idPedido}`;
        document.getElementById('preview-subtitle').textContent = `${clienteNome(ultimoPedido.idCliente)} • ${money.format(ultimoPedido.valorTotal || 0)}`;
    }

    const tbody = document.getElementById('dashboard-pedidos');
    const recentes = [...state.pedidos].sort((a, b) => (b.idPedido || 0) - (a.idPedido || 0)).slice(0, 4);
    tbody.innerHTML = recentes.map(pedido => `
        <tr>
            <td>#${pedido.idPedido}</td>
            <td>${clienteNome(pedido.idCliente)}</td>
            <td><span class="status">${statusTexto(pedido.statusAtual)}</span></td>
            <td>${money.format(pedido.valorTotal || 0)}</td>
        </tr>
    `).join('') || '<tr><td colspan="4">Nenhum pedido cadastrado ainda.</td></tr>';
}

function renderClientes() {
    const container = document.getElementById('lista-clientes');
    const clientes = [...state.clientes].sort((a, b) => (b.idCliente || 0) - (a.idCliente || 0));
    const pagina = itensDaPagina('clientes', clientes);

    container.innerHTML = pagina.map(cliente => `
        <article class="data-card">
            <div>
                <strong>👤 ${cliente.nome}</strong>
                <span>${cliente.email || 'E-mail não informado'} • ${cliente.telefone || 'Telefone não informado'}</span><br>
                <span>${cliente.cidade || 'Cidade não informada'}${cliente.estado ? `/${cliente.estado}` : ''}</span>
            </div>
            <div class="price">#${cliente.idCliente}</div>
        </article>
    `).join('') || '<p class="muted">Nenhum cliente cadastrado.</p>';

    renderPaginacao('clientes', clientes);
}

function renderProdutos() {
    const container = document.getElementById('lista-produtos');
    const produtos = [...state.produtos].sort((a, b) => (b.idProduto || 0) - (a.idProduto || 0));
    const pagina = itensDaPagina('produtos', produtos);

    container.innerHTML = pagina.map(produto => `
        <article class="product-card">
            <div class="product-main">
                <div class="food-marker">${foodEmoji(produto.nome)}</div>
                <div>
                    <strong>${produto.nome}</strong>
                    <span>Estoque: ${produto.estoque ?? 0} unidade(s)</span>
                </div>
            </div>
            <div class="price">${money.format(produto.preco || 0)}</div>
        </article>
    `).join('') || '<p class="muted">Nenhum produto cadastrado.</p>';

    renderPaginacao('produtos', produtos);
}

function renderPedidos() {
    const tbody = document.getElementById('tabela-pedidos');
    const pedidos = [...state.pedidos].sort((a, b) => (b.idPedido || 0) - (a.idPedido || 0));
    const pagina = itensDaPagina('pedidos', pedidos);

    tbody.innerHTML = pagina.map(pedido => `
        <tr>
            <td>#${pedido.idPedido}</td>
            <td>${clienteNome(pedido.idCliente)}</td>
            <td><span class="status">${statusTexto(pedido.statusAtual)}</span></td>
            <td>${money.format(pedido.valorTotal || 0)}</td>
            <td>
                <div class="action-row">
                    <button class="ghost small" onclick="atualizarStatus(${pedido.idPedido}, 'PENDENTE')">Pendente</button>
                    <button class="ghost small" onclick="atualizarStatus(${pedido.idPedido}, 'EM_PREPARO')">Preparo</button>
                    <button class="ghost small" onclick="atualizarStatus(${pedido.idPedido}, 'ENVIADO')">Enviado</button>
                    <button class="ghost small" onclick="atualizarStatus(${pedido.idPedido}, 'ENTREGUE')">Entregue</button>
                </div>
            </td>
        </tr>
    `).join('') || '<tr><td colspan="5">Nenhum pedido cadastrado.</td></tr>';

    renderPaginacao('pedidos', pedidos);
}

function renderPagamentos() {
    const container = document.getElementById('lista-pagamentos');
    const pagamentos = [...state.pagamentos].sort((a, b) => (b.idPagamento || 0) - (a.idPagamento || 0));
    const pagina = itensDaPagina('pagamentos', pagamentos);

    container.innerHTML = pagina.map(pagamento => `
        <article class="payment-card">
            <div>
                <strong>💳 Pedido #${pagamento.idPedido} • ${pagamento.metodoPagamento || 'Método não informado'}</strong>
                <span>${pagamento.statusPagamento || 'Status não informado'} • ${dataTexto(pagamento.dataPagamento)}</span>
            </div>
            <div class="price">${money.format(pagamento.valor || 0)}</div>
        </article>
    `).join('') || '<p class="muted">Nenhum pagamento registrado.</p>';

    renderPaginacao('pagamentos', pagamentos);
}

function renderHistorico() {
    const timeline = document.getElementById('timeline');
    const historico = [...state.historico].sort((a, b) => (b.idHistorico || 0) - (a.idHistorico || 0));
    const pagina = itensDaPagina('historico', historico);

    timeline.innerHTML = pagina.map(item => `
        <article class="timeline-item">
            <span class="timeline-dot"></span>
            <div>
                <strong>Pedido #${item.idPedido} • ${statusTexto(item.status)}</strong>
                <span>${dataTexto(item.dataHora)}</span>
            </div>
        </article>
    `).join('') || '<p class="muted">Nenhum histórico registrado.</p>';

    renderPaginacao('historico', historico);
}

function renderEntregas() {
    const container = document.getElementById('lista-entregas');
    const entregas = [...state.entregas].sort((a, b) => (b.idEntrega || 0) - (a.idEntrega || 0));
    const pagina = itensDaPagina('entregas', entregas);

    container.innerHTML = pagina.map(entrega => `
        <article class="delivery-card">
            <strong>🛵 Pedido #${entrega.idPedido}</strong>
            <span>${entrega.statusEntrega || 'Sem status'} • ${entrega.transporte || 'Transporte não informado'}</span><br>
            <span>Código: ${entrega.codigoRastreio || 'Não informado'}</span>
        </article>
    `).join('') || '<p class="muted">Nenhuma entrega cadastrada.</p>';

    renderPaginacao('entregas', entregas);
}

function preencherCombos() {
    preencherSelect('select-cliente', state.clientes, 'idCliente', c => `${c.nome} (#${c.idCliente})`, 'Cadastre um cliente primeiro');
    preencherSelect('select-produto', state.produtos, 'idProduto', p => `${p.nome} - ${money.format(p.preco || 0)}`, 'Cadastre um produto primeiro');
    const pedidosLabel = p => `Pedido #${p.idPedido} - ${clienteNome(p.idCliente)} - ${money.format(p.valorTotal || 0)}`;
    preencherSelect('select-pedido-item', state.pedidos, 'idPedido', pedidosLabel, 'Crie um pedido primeiro');
    preencherSelect('select-pedido-pagamento', state.pedidos, 'idPedido', pedidosLabel, 'Crie um pedido primeiro');
    preencherSelect('select-pedido-entrega', state.pedidos, 'idPedido', pedidosLabel, 'Crie um pedido primeiro');
}

function renderTudo() {
    renderDashboard();
    renderClientes();
    renderProdutos();
    renderPedidos();
    renderPagamentos();
    renderHistorico();
    renderEntregas();
    preencherCombos();
}

async function carregarTudo() {
    try {
        const [clientes, produtos, pedidos, itens, pagamentos, entregas, historico] = await Promise.all([
            request(endpoints.clientes),
            request(endpoints.produtos),
            request(endpoints.pedidos),
            request(endpoints.itens),
            request(endpoints.pagamentos),
            request(endpoints.entregas),
            request(endpoints.historico)
        ]);

        Object.assign(state, { clientes, produtos, pedidos, itens, pagamentos, entregas, historico });
        document.getElementById('api-status').textContent = 'Online';
        renderTudo();
    } catch (error) {
        document.getElementById('api-status').textContent = 'Offline';
        toast(error.message, 'error');
    }
}
window.carregarTudo = carregarTudo;

async function atualizarStatus(idPedido, status) {
    try {
        await request(`/pedidos/${idPedido}?novoStatus=${encodeURIComponent(status)}`, { method: 'PUT' });
        toast(`Pedido #${idPedido} atualizado para ${statusTexto(status)}.`);
        await carregarTudo();
    } catch (error) {
        toast(error.message, 'error');
    }
}
window.atualizarStatus = atualizarStatus;

function bindForm(formId, url, successMessage) {
    const form = document.getElementById(formId);
    if (!form) return;
    form.addEventListener('submit', async event => {
        event.preventDefault();
        try {
            const body = formToObject(form);
            await request(url, { method: 'POST', body: JSON.stringify(body) });
            form.reset();
            toast(successMessage);
            await carregarTudo();
        } catch (error) {
            toast(error.message, 'error');
        }
    });
}

function activateScreen(name) {
    const screenName = name || 'dashboard';
    document.querySelectorAll('.screen').forEach(screen => screen.classList.remove('active'));
    document.querySelectorAll('.nav-link').forEach(link => link.classList.remove('active'));

    const target = document.getElementById(`screen-${screenName}`) || document.getElementById('screen-dashboard');
    target.classList.add('active');

    const activeLink = document.querySelector(`.nav-link[href="#${screenName}"]`) || document.querySelector('.nav-link[href="#dashboard"]');
    if (activeLink) activeLink.classList.add('active');
}

function syncScreenFromHash() {
    const hash = window.location.hash.replace('#', '') || 'dashboard';
    activateScreen(hash);
}

function bindNavigation() {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', event => {
            const target = event.currentTarget.getAttribute('href').replace('#', '');
            activateScreen(target);
        });
    });

    document.querySelectorAll('[data-screen]').forEach(button => {
        button.addEventListener('click', () => {
            const target = button.dataset.screen;
            window.location.hash = target;
            activateScreen(target);
        });
    });

    window.addEventListener('hashchange', syncScreenFromHash);
}

bindForm('form-cliente', endpoints.clientes, 'Cliente cadastrado com sucesso.');
bindForm('form-produto', endpoints.produtos, 'Produto cadastrado com sucesso.');
bindForm('form-pedido', endpoints.pedidos, 'Pedido criado com sucesso.');
bindForm('form-item', endpoints.itens, 'Item adicionado ao pedido.');
bindForm('form-pagamento', endpoints.pagamentos, 'Pagamento registrado.');
bindForm('form-entrega', endpoints.entregas, 'Entrega registrada.');

window.addEventListener('DOMContentLoaded', () => {
    bindNavigation();
    syncScreenFromHash();
    carregarTudo();
});
